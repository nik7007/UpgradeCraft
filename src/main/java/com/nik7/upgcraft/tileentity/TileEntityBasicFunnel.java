package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.BlockBasicFunnel;
import com.nik7.upgcraft.reference.ConfigOptions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;


public class TileEntityBasicFunnel extends TileEntityFunnel {

    private final static Random RANDOM = new Random();
    private boolean burned = false;
    private int burnTicks = RANDOM.nextInt(150) + 1;
    private int realSpeed = speed;

    public TileEntityBasicFunnel() {
        super(ConfigOptions.BASIC_FUNNEL_SPEED);
    }

    protected boolean canOperate(IBlockState blockState) {

        if (!this.burned)
            this.burned = blockState.getValue(BlockBasicFunnel.BURNED);
        return this.burned || super.canOperate(blockState);
    }


    private boolean isFluidHot() {
        FluidStack fluidStack = fluidTank.getFluid();
        return fluidStack != null && fluidStack.getFluid().getTemperature(fluidStack) > ConfigOptions.WOOD_BURN_TEMPERATURE;
    }

    private void burnAction() {

        if (!this.burned && isFluidHot()) {
            if (this.burnTicks <= 0) {
                IBlockState state = getWorld().getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof BlockBasicFunnel)
                    ((BlockBasicFunnel) block).burnFunnel(getWorld(), pos, state, RANDOM);

            } else this.burnTicks--;


        }
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            if (this.burned && this.realSpeed == speed)
                this.realSpeed += speed / 10;

            funnelAction(realSpeed);
        }
        this.burnAction();
    }


}
