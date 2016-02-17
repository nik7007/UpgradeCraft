package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockUpgCClayFluidTank extends BlockUpgCTank {

    private Random random = new Random();

    public static final PropertyBool IS_HARDENED = PropertyBool.create("isHardened");

    public BlockUpgCClayFluidTank() {
        super(Material.rock, Capacity.SMALL_TANK, "ClayFluidTank");
        this.setHardness(2.8f);
        this.hasSubBlocks = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, TankType.SOLID).withProperty(IS_HARDENED, false));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (this.hasSubBlocks) {
            list.add(new ItemStack(this, 1, TankType.SOLID.getMeta()));
            list.add(new ItemStack(this, 1, TankType.GLASSES.getMeta()));
            list.add(new ItemStack(this, 1, TankType.SOLID.getMeta() + 2));
            list.add(new ItemStack(this, 1, TankType.GLASSES.getMeta() + 2));
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE, IS_HARDENED);
    }

    public void hardenedClayTank(World world, BlockPos pos, IBlockState state) {

        if (!state.getValue(IS_HARDENED)) {

            TileEntity tileentity = world.getTileEntity(pos);

            world.setBlockState(pos, state.withProperty(IS_HARDENED, true));

            if (world.isRemote)
                spawnParticles(world, pos, random, EnumParticleTypes.SMOKE_LARGE);

            world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);

            if (tileentity != null) {
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }

        }


    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (state.getValue(IS_HARDENED))
            return;

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof UpgCtileentityClayFluidTank) {
            if (((UpgCtileentityClayFluidTank) tileEntity).isCooking())
                spawnParticles(worldIn, pos, rand, EnumParticleTypes.FLAME);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof UpgCtileentityClayFluidTank && stack.hasTagCompound()) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
            if (fluidStack != null) {
                ((UpgCtileentityClayFluidTank) tileEntity).fill(EnumFacing.NORTH, fluidStack, true);
            }
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        player.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
        player.addExhaustion(0.025F);

        if (state.getValue(IS_HARDENED) && this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getSilkTouchModifier(player)) {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(state);

            if (itemstack != null) {
                if (te instanceof UpgCtileentityClayFluidTank) {
                    FluidStack fluidStack = ((UpgCtileentityClayFluidTank) te).getFluidFormSingleTank();
                    if (fluidStack != null) {
                        itemstack.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
                    }
                }

                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);
            for (ItemStack stack : items) {
                spawnAsEntity(worldIn, pos, stack);
            }
        } else {
            harvesters.set(player);
            int i = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        IBlockState blockState = world.getBlockState(pos);
        ItemStack result = new ItemStack(this, 1, this.getMetaFromState(blockState));

        TileEntity tileEntity = world.getTileEntity(pos);
        if (blockState.getValue(IS_HARDENED) && tileEntity instanceof UpgCtileentityClayFluidTank) {

            FluidStack fluidStack = ((UpgCtileentityClayFluidTank) tileEntity).getFluidFormSingleTank();
            if (fluidStack != null) {
                result.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));
            }

        }


        return result;
    }

    @Override
    public int damageDropped(IBlockState state) {
        int oldMeta = super.damageDropped(state);

        if (state.getValue(IS_HARDENED))
            oldMeta += 2;
        return oldMeta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isCocked = false;
        if (meta >= 2) {
            meta -= 2;
            isCocked = true;
        }
        return super.getStateFromMeta(meta).withProperty(IS_HARDENED, isCocked);
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityClayFluidTank();
    }
}
