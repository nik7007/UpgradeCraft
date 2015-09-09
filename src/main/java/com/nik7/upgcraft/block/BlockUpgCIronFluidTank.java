package com.nik7.upgcraft.block;


import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankIron;
import com.nik7.upgcraft.util.BlockToItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class BlockUpgCIronFluidTank extends BlockUpgCTank {

    public BlockUpgCIronFluidTank() {
        super(Material.iron);
        this.setBlockName(Names.Blocks.IRON_LIQUID_TANK);
        this.setHardness(2.8f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        this.setStepSound(soundTypeStone);
        this.haveSubBlocks = true;
        this.capacity = 2 * Capacity.SMALL_TANK;
        this.setBlockTextureName(Texture.Blocks.IRON_LIQUID_TANK);
    }

    @Override
    public int getRenderType() {
        return Render.Ids.FLUID_TANK;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }


    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        if (world.getBlock(x, y - 1, z) == this)
            if (world.getBlock(x, y - 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this)
            if (world.getBlock(x, y + 2, z) == this)
                return false;
        return !(world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this);

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {

        if (!world.isRemote) {
            UpgCtileentityTankIron tileEntity = (UpgCtileentityTankIron) world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());
                if (fluidStack != null) {
                    tileEntity.fill(ForgeDirection.UNKNOWN, fluidStack, true);
                }
            }
        }


    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> result = BlockToItemHelper.getDrops(x, y, z, world.provider.dimensionId);

        if (result == null)
            result = super.getDrops(world, x, y, z, metadata, fortune);
        return result;

    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {

        int meta = world.getBlockMetadata(x, y, z);
        ItemStack itemTank = new ItemStack(this, 1, meta);

        UpgCtileentityTankIron tileEntity = (UpgCtileentityTankIron) world.getTileEntity(x, y, z);

        if (tileEntity != null) {

            FluidStack fluidStack = tileEntity.getFluidFormSingleTank();

            if (fluidStack != null) {
                if (!itemTank.hasTagCompound())
                    itemTank.stackTagCompound = new NBTTagCompound();
                fluidStack.writeToNBT(itemTank.stackTagCompound);
            }
        }

        return itemTank;
    }

    @Override
    public void appliedConfig(SystemConfig.ConfigValue... values) {

        if (values.length >= 1) {
            for (SystemConfig.ConfigValue c : values) {

                if (c.configName.equals("basicTankCapacity")) {

                    this.capacity = 2 * new Integer(c.value);

                }

            }

        }

    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityTankIron();
    }
}
