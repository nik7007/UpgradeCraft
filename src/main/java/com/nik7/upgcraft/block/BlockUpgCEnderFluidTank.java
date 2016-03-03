package com.nik7.upgcraft.block;


import com.nik7.upgcraft.entity.player.ExtendedPlayerUpgC;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityEnderFluidTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockUpgCEnderFluidTank extends BlockUpgCTank {


    public BlockUpgCEnderFluidTank() {
        super(Material.rock, Capacity.SMALL_TANK, "EnderFluidTank");
        this.setHardness(22.5F);
        this.setResistance(1000.0F);
        this.setStepSound(soundTypePiston);
        this.hasSubBlocks = false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        for (int l = 0; l < 3; ++l) {
            double d1 = (double) ((float) pos.getY() + rand.nextFloat());
            double d3;
            double d4;
            double d5;
            int i1 = rand.nextInt(2) * 2 - 1;
            int j1 = rand.nextInt(2) * 2 - 1;
            d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
            double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j1;
            d5 = (double) (rand.nextFloat() * 1.0F * (float) j1);
            double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) i1;
            d3 = (double) (rand.nextFloat() * 1.0F * (float) i1);
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }

    }

    @Override
    protected void handleContainerClick(World worldIn, BlockPos pos, EntityPlayer playerIn, UpgCtileentityFluidTank tank, ItemStack equippedItemStack) {

        if (FluidContainerRegistry.isBucket(equippedItemStack)) {

            ((UpgCtileentityEnderFluidTank) tank).setFluidTank(ExtendedPlayerUpgC.getUpgCEnderTank(playerIn));
            super.handleContainerClick(worldIn, pos, playerIn, tank, equippedItemStack);
            ((UpgCtileentityEnderFluidTank) tank).setFluidTank(new FluidTank(0));
        }

    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.blockUpgCSlimyObsidian);
    }

    @Override
    public int quantityDropped(Random random) {
        return 8;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }


    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.obsidianColor;
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityEnderFluidTank();
    }
}
