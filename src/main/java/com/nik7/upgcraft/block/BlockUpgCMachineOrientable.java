package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockUpgCMachineOrientable extends BlockUpgCContainerOrientable {


    public BlockUpgCMachineOrientable(Material material, String name) {
        super(material, name);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof UpgCtileentityInventoryFluidHandler)
            return ((UpgCtileentityInventoryFluidHandler) te).getFluidLight();
        return super.getLightValue(state, world, pos);
    }

}
