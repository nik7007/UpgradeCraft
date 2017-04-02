package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityHardenedClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockHardenedClayFluidTank extends BlockFluidTank {

    public BlockHardenedClayFluidTank() {
        super("hardenedclayfluidtank", Material.CLAY);
        this.setHardness(2.5f);
    }


    private FluidStack getFluid(ItemStack itemStack) {

        IFluidHandlerItem tank = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (tank != null) {
            return tank.getTankProperties()[0].getContents();
        }
        return null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        FluidStack fluidStack = this.getFluid(stack);
        if (fluidStack != null) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityHardenedClayFluidTank) {
                ((TileEntityHardenedClayFluidTank) tileEntity).fill(fluidStack, true);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack result = new ItemStack(this, 1, this.getMetaFromState(state));

        TileEntity tileEntity = world.getTileEntity(pos);
        FluidStack fluidStack = null;
        if (tileEntity instanceof TileEntityHardenedClayFluidTank)
            fluidStack = ((TileEntityHardenedClayFluidTank) tileEntity).getFluid();

        if (fluidStack != null) {
            IFluidHandlerItem tank = result.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (tank != null) {
                tank.fill(fluidStack, true);
            }
        }

        return result;

    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {

        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            List<ItemStack> items = new ArrayList<>();
            ItemStack itemstack = this.getSilkTouchDrop(state);

            if (te instanceof TileEntityHardenedClayFluidTank) {
                FluidStack fluid = ((TileEntityHardenedClayFluidTank) te).getFluid();
                if (fluid != null) {
                    IFluidHandlerItem tank = itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                    if (tank != null)
                        tank.fill(fluid, true);
                }
            }
            if (!itemstack.isEmpty()) {
                items.add(itemstack);
            }

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack item : items) {
                spawnAsEntity(worldIn, pos, item);
            }
        } else {
            harvesters.set(player);
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHardenedClayFluidTank();
    }
}
