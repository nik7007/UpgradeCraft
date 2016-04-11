package com.nik7.upgcraft.block;


import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityIronFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class BlockUpgCIronFluidTank extends BlockUpgCFluidTank {


    public BlockUpgCIronFluidTank() {
        super(Material.iron, Capacity.SMALL_TANK * 2, "IronFluidTank");
        this.hasSubBlocks = true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof UpgCtileentityIronFluidTank && stack.hasTagCompound()) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
            if (fluidStack != null) {
                ((UpgCtileentityIronFluidTank) tileEntity).fill(EnumFacing.NORTH, fluidStack, true);
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {

        if (te instanceof UpgCtileentityIronFluidTank) {

            player.addStat(StatList.func_188055_a(this));
            player.addExhaustion(0.025F);

            ArrayList<ItemStack> items = new ArrayList<>();
            ItemStack itemstack = this.createStackedBlock(state);

            if (itemstack != null) {

                FluidStack fluidStack = ((UpgCtileentityIronFluidTank) te).getFluidFormSingleTank();
                if (fluidStack != null) {
                    itemstack.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
                }

                items.add(itemstack);
            }
            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);
            for (ItemStack itemStack : items) {
                spawnAsEntity(worldIn, pos, itemStack);
            }


        } else super.harvestBlock(worldIn, player, pos, state, te, stack);

    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer
            player) {
        IBlockState blockState = world.getBlockState(pos);
        ItemStack result = new ItemStack(this, 1, this.getMetaFromState(blockState));

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof UpgCtileentityIronFluidTank) {

            FluidStack fluidStack = ((UpgCtileentityIronFluidTank) tileEntity).getFluidFormSingleTank();
            if (fluidStack != null) {
                result.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
            }

        }


        return result;
    }

    @Override
    public void appliedConfig(SystemConfig.ConfigValue... values) {

        if (values.length >= 1) {
            for (SystemConfig.ConfigValue c : values) {

                if (c.configName.equals("basicTankCapacity")) {
                    this.capacity = new Integer(c.value) * 2;

                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityIronFluidTank();
    }
}
