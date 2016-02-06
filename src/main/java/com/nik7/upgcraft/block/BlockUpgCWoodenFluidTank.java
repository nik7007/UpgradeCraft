package com.nik7.upgcraft.block;


import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockUpgCWoodenFluidTank extends BlockUpgCTank {

    private boolean canBurn = true;

    public BlockUpgCWoodenFluidTank() {
        super(Material.wood, Capacity.SMALL_TANK, "WoodenTank");
        this.setTickRandomly(true);
        this.hasSubBlocks = true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (!canBurn) return;

        UpgCtileentityWoodenFluidTank entity = (UpgCtileentityWoodenFluidTank) worldIn.getTileEntity(pos);

        if (entity.isFluidHot())
            spawnParticles(worldIn, pos, rand, EnumParticleTypes.SMOKE_NORMAL);


    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

        if (!canBurn)
            return 0;

        UpgCtileentityWoodenFluidTank tank = (UpgCtileentityWoodenFluidTank) world.getTileEntity(pos);

        if (tank != null && tank.isFluidHot())
            return 75;

        return 5;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

        if (!canBurn)
            return 0;

        return 10;
    }

    @Override
    public int tickRate(World worldIn) {
        return 8;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (!canBurn)
            return;


        UpgCtileentityWoodenFluidTank entity = (UpgCtileentityWoodenFluidTank) worldIn.getTileEntity(pos);

        if (entity.isFluidHot()) {

            if (!setInFire(worldIn, pos))
                setInFireNeighbors(worldIn, pos, rand);

        }

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
            world.setBlockState(pos, Blocks.fire.getDefaultState(), 3);
            return true;
        } else return false;

    }

    @Override
    public void appliedConfig(SystemConfig.ConfigValue... values) {
        if (values.length >= 1) {
            super.appliedConfig(values);
            for (SystemConfig.ConfigValue c : values) {

                if (c.configName.equals("basicWoodenBlockFlammability")) {
                    this.canBurn = c.value.toLowerCase().equals("true");
                }
            }
        }

    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityWoodenFluidTank();
    }
}
