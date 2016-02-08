package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCContainerOrientable;
import com.nik7.upgcraft.reference.Capacity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class UpgCtilientityBasicFluidHopper extends UpgCtileentityTank {

	public UpgCtilientityBasicFluidHopper() {
		super(Capacity.FLUID_HOPPER_TANK, false);

	}
	
	@Override
    public void update() {
		
		if(!worldObj.isRemote){
			
			IBlockState blockState = worldObj.getBlockState(pos);
			EnumFacing enumfacing = blockState.getValue(BlockUpgCContainerOrientable.FACING);
			boolean hasToWork = worldObj.isBlockIndirectlyGettingPowered(pos) == 0;
			
			if(hasToWork){
				
				
			}
			
			
		}
		
	}

	@Override
	protected boolean canMerge(TileEntity tileEntity) {
		return false;
	}

}
