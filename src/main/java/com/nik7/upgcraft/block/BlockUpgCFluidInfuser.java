package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockUpgCFluidInfuser extends BlockUpgCContainerOrientable {

    private final static Random rand = new Random();

    public BlockUpgCFluidInfuser() {
        super(Material.iron, "FluidInfuser");
        setStepSound(soundTypePiston);
        setHardness(5.2F);
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        UpgCtileentityFluidInfuser fluidFurnace = (UpgCtileentityFluidInfuser) worldIn.getTileEntity(pos);

        if (fluidFurnace.isActive()) {
            spawnParticles(worldIn, pos, state, rand, EnumParticleTypes.SMOKE_NORMAL, EnumParticleTypes.FLAME);
        }

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(UpgradeCraft.instance, GUIs.FLUID_INFUSER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof UpgCtileentityFluidInfuser) {

            UpgCtileentityFluidInfuser teFF = (UpgCtileentityFluidInfuser) te;

            ItemStack itemStackIn = teFF.getStackInSlot(UpgCtileentityFluidInfuser.INFUSE);
            ItemStack itemStackInP = teFF.getStackInSlot(UpgCtileentityFluidInfuser.INFUSE_P);
            ItemStack itemStackMelt = teFF.getStackInSlot(UpgCtileentityFluidInfuser.MELT);
            ItemStack itemStackOut = teFF.getStackInSlot(UpgCtileentityFluidInfuser.OUTPUT);

            dropItems(worldIn, pos, itemStackIn, rand);
            dropItems(worldIn, pos, itemStackInP, rand);
            dropItems(worldIn, pos, itemStackMelt, rand);
            dropItems(worldIn, pos, itemStackOut, rand);

            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityFluidInfuser();
    }
}
