package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluidFurnace extends BlockOrientable implements ITileEntityProvider {
    public BlockFluidFurnace() {
        super(Material.ROCK, "fluidfurnace");
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if (stack.hasDisplayName()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityFluidFurnace) {
                ((TileEntityFluidFurnace) te).setCustomInventoryName(stack.getDisplayName());
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos pos, Random rand) {

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityFluidFurnace) {
            if (((TileEntityFluidFurnace) te).isWorking()) {
                spawnParticles(world, pos, rand, EnumParticleTypes.SMOKE_NORMAL, EnumParticleTypes.FLAME);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityFluidFurnace) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFluidFurnace) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityFluidHandler)
            return ((TileEntityFluidHandler) te).getFluidLight();

        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidFurnace();
    }
}
