package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.TestTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TestBlock extends BlockUpgC implements ITileEntityProvider {

    public TestBlock() {
        super(Material.cake, "test");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TestTE();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TestTE)
                return ((TestTE) te).getLight();

        return super.getLightValue(state, world, pos);
    }
}
