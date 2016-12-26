package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.ConfigOptions;
import com.nik7.upgcraft.tileentity.TileEntityWoodenFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockWoodenFluidTank extends BlockFluidTank {


    public BlockWoodenFluidTank() {
        super("woodenfluidtank");
        if (ConfigOptions.WOODEN_TANK_BURN)
            this.setTickRandomly(true);
        this.setHardness(2.5f);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos pos, Random rand) {

        if (!ConfigOptions.WOODEN_TANK_BURN)
            return;

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityWoodenFluidTank)
            if (((TileEntityWoodenFluidTank) te).isFluidHot()) {
                spawnParticles(world, pos, rand, EnumParticleTypes.SMOKE_NORMAL);
                world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }

    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

        if (!ConfigOptions.WOODEN_TANK_BURN)
            return 0;

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityWoodenFluidTank)
            if (((TileEntityWoodenFluidTank) te).isFluidHot())
                return 75;

        return 5;
    }


    @Override
    public int tickRate(World worldIn) {
        return 8;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

        if (!ConfigOptions.WOODEN_TANK_BURN)
            return 0;

        return 10;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (!ConfigOptions.WOODEN_TANK_BURN)
            return;


        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityWoodenFluidTank)
            if (((TileEntityWoodenFluidTank) te).isFluidHot())
                if (!setInFire(worldIn, pos))
                    setInFireNeighbors(worldIn, pos, rand);

    }

    private boolean setInFireNeighbors(World world, BlockPos pos, Random rand) {

        int r = rand.nextInt(6);

        switch (r) {
            case 0:
                if (WorldHelper.getBlock(world, pos.up()).isFlammable(world, pos.up(), EnumFacing.DOWN))
                    return setInFire(world, pos.up());
                break;

            case 1:
                if (WorldHelper.getBlock(world, pos.down()).isFlammable(world, pos.down(), EnumFacing.UP))
                    return setInFire(world, pos.down());
                break;
            case 2:
                if (WorldHelper.getBlock(world, pos.east()).isFlammable(world, pos.east(), EnumFacing.WEST))
                    return setInFire(world, pos.east());
                break;
            case 3:
                if (WorldHelper.getBlock(world, pos.west()).isFlammable(world, pos.west(), EnumFacing.EAST))
                    return setInFire(world, pos.west());
                break;
            case 4:
                if (WorldHelper.getBlock(world, pos.north()).isFlammable(world, pos.north(), EnumFacing.SOUTH))
                    return setInFire(world, pos.north());
                break;
            case 5:
                if (WorldHelper.getBlock(world, pos.south()).isFlammable(world, pos.south(), EnumFacing.NORTH))
                    return setInFire(world, pos.south());
                break;

        }
        return false;
    }

    private boolean setInFire(World world, BlockPos pos) {
        return setBlockInFire(world, pos.up()) || setBlockInFire(world, pos.down()) || setBlockInFire(world, pos.east()) || setBlockInFire(world, pos.north()) || setBlockInFire(world, pos.west()) || setBlockInFire(world, pos.south());

    }

    private boolean setBlockInFire(World world, BlockPos pos) {

        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
            return true;
        } else return false;

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWoodenFluidTank();
    }
}
