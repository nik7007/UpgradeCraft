package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class BlockUpgCFluidFurnace extends BlockUpgCContainerOrientable {

    private final Random rand = new Random();

    public BlockUpgCFluidFurnace() {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_FURNACE);
        this.setBlockTextureName("cobblestone");
        setStepSound(soundTypePiston);
        setHardness(5.0F);

    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    public int getRenderType() {
        return RenderIds.FLUID_MACHINE;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(UpgradeCraft.instance, GUIs.FLUID_FURNACE.ordinal(), world, x, y, z);

        }
        return true;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        UpgCtileentityFluidFurnace te = (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z);
        if (te.isActive) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.2F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            switch (l) {
                case 4:
                    world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 5:
                    world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 2:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    break;
                case 3:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityFluidFurnace te = (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z);

        ItemStack itemStackIn = te.getStackInSlot(UpgCtileentityFluidFurnace.INPUT);
        ItemStack itemStackOut = te.getStackInSlot(UpgCtileentityFluidFurnace.OUTPUT);

        dropItems(world, x, y, z, itemStackIn, rand);
        dropItems(world, x, y, z, itemStackOut, rand);

        super.breakBlock(world, x, y, z, block, n);

    }


    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        UpgCtileentityFluidFurnace tile = ((UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z));
        if (tile.fluidLevel > 0) {
            return FluidRegistry.getFluid("lava").getLuminosity();
        } else
            return 0;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityFluidFurnace();
    }
}
