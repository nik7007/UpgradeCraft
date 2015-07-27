package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankClay;
import com.nik7.upgcraft.util.BlockToItemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockClayFluidTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon icon;
    private IIcon iconHarden;

    public BlockClayFluidTank() {
        super(Material.clay);
        this.setBlockName(Names.Blocks.CLAY_LIQUID_TANK);
        this.setHardness(2.8f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        this.setStepSound(soundTypeStone);

        this.capacity = Capacity.SMALL_TANK;
    }


    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

        subItems.add(new ItemStack(this, 1, 0));
        subItems.add(new ItemStack(this, 1, 1));
        subItems.add(new ItemStack(this, 1, 2));
        subItems.add(new ItemStack(this, 1, 3));


    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof UpgCtileentityTankClay) {
            int meta = entity.getBlockMetadata();

            if (meta < 2) {
                boolean hot = ((UpgCtileentityTankClay) entity).isCooking;
                if (hot) {
                    spawnParticle(world, x, y, z, random, "smoke");
                }
            }
        }
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon(Texture.Blocks.CLAY_LIQUID_TANK);
        this.iconHarden = iconRegister.registerIcon(Texture.Blocks.HARDENED_CLAY_LIQUID_TANK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 2)
            return icon;
        else
            return iconHarden;
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
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1 || meta == 3) {
            return ((UpgCtileentityTank) world.getTileEntity(x, y, z)).getFluidLightLevel();
        } else
            return 0;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {

        if (!world.isRemote) {
            UpgCtileentityTankClay tileEntity = (UpgCtileentityTankClay) world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());
                if (fluidStack != null) {
                    tileEntity.fill(ForgeDirection.UNKNOWN, fluidStack, true);
                }
            }
        }


    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta == 3 && this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player)) {
            UpgCtileentityTank upgCtileentityTank = (UpgCtileentityTank) world.getTileEntity(x, y, z);
            ArrayList<ItemStack> itemStacks = getDrops(world, x, y, z, meta, 0);

            if (!itemStacks.isEmpty() && upgCtileentityTank != null) {

                int capacity = (int) upgCtileentityTank.getCapacity();
                FluidStack fluidStack = upgCtileentityTank.getFluid();

                if (fluidStack != null) {
                    for (ItemStack itemStack : itemStacks) {
                        if (!itemStack.hasTagCompound())
                            itemStack.stackTagCompound = new NBTTagCompound();
                        itemStack.getTagCompound().setInteger("capacity", capacity);
                        fluidStack.writeToNBT(itemStack.getTagCompound());
                    }
                }

                BlockToItemHelper.addDrops(x, y, z, world.provider.dimensionId, itemStacks);

            }

        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player)) {
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, 0);
            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items) {
                this.dropBlockAsItem(world, x, y, z, is);
            }
        } else {
            harvesters.set(player);
            int i1 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(world, x, y, z, meta, i1);
            harvesters.set(null);
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

        UpgCtileentityTankClay tileEntity = meta < 2 ? null : (UpgCtileentityTankClay) world.getTileEntity(x, y, z);

        if (tileEntity != null) {
            if (!itemTank.hasTagCompound())
                itemTank.stackTagCompound = new NBTTagCompound();
            tileEntity.getFluidFormSingleTank().writeToNBT(itemTank.stackTagCompound);
            itemTank.getTagCompound().setInteger("capacity", (int) tileEntity.getCapacity());
        }

        return itemTank;
    }


    @Override
    protected boolean canSilkHarvest() {
        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityTankClay();
    }

}
