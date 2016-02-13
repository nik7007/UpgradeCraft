package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockUpgCFluidFurnace extends BlockUpgCContainerOrientable {

    public BlockUpgCFluidFurnace() {
        super(Material.iron, "FluidFurnace");
        setStepSound(soundTypePiston);
        setHardness(5.0F);
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

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof UpgCtileentityFluidFurnace) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (UpgCtileentityFluidFurnace) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityFluidFurnace();
    }
}
