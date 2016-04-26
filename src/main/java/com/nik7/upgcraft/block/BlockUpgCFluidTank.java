package com.nik7.upgcraft.block;


import com.nik7.upgcraft.config.ConfigurableObject;
import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class BlockUpgCFluidTank extends BlockUpgC implements ITileEntityProvider, ConfigurableObject {


    protected int capacity;
    public static final PropertyEnum<TankType> TYPE = PropertyEnum.create("type", TankType.class);
    protected boolean hasSubBlocks = false;
    private static final AxisAlignedBB BB = new AxisAlignedBB(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);

    public BlockUpgCFluidTank(Material material, int capacity, String name) {
        super(material, name);
        this.capacity = capacity;
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, TankType.SOLID));
        //this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        UpgCtileentityFluidTank tank = (UpgCtileentityFluidTank) worldIn.getTileEntity(pos);
        tank.findAdjTank();

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        UpgCtileentityFluidTank tank = (UpgCtileentityFluidTank) worldIn.getTileEntity(pos);
        tank.separateTanks();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        ItemStack equippedItemStack = playerIn.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

        if (FluidContainerRegistry.isContainer(equippedItemStack)) {

            if (!worldIn.isRemote) {
                UpgCtileentityFluidTank tank = (UpgCtileentityFluidTank) worldIn.getTileEntity(pos);
                handleContainerClick(worldIn, pos, playerIn, tank, equippedItemStack);
            }

            return true;

        }


        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    protected void handleContainerClick(World worldIn, BlockPos pos, EntityPlayer playerIn, UpgCtileentityFluidTank tank, ItemStack equippedItemStack) {

        if (FluidContainerRegistry.isBucket(equippedItemStack)) {

            if (FluidContainerRegistry.isEmptyContainer(equippedItemStack)) {
                fillBucketFromTank(worldIn, pos, playerIn, tank, equippedItemStack);
            } else {
                drainBucketIntoTank(playerIn, tank, equippedItemStack);
            }
        }

    }

    private void fillBucketFromTank(World worldIn, BlockPos pos, EntityPlayer playerIn, UpgCtileentityFluidTank tank, ItemStack equippedItemStack) {

        int bucketVolume = FluidContainerRegistry.BUCKET_VOLUME;
        FluidStack drained = tank.drain(null, bucketVolume, false);

        if (drained != null && drained.amount == bucketVolume) {

            ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(drained, FluidContainerRegistry.EMPTY_BUCKET);

            if (filledBucket != null) {
                tank.drain(null, bucketVolume, true);
                if (!playerIn.capabilities.isCreativeMode) {

                    if (equippedItemStack.stackSize == 1) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, filledBucket);
                        return;
                    }

                    equippedItemStack.stackSize--;
                    if (!playerIn.inventory.addItemStackToInventory(filledBucket)) {
                        worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, filledBucket));

                    } else if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }

                }
            }


        }

    }

    protected void drainBucketIntoTank(EntityPlayer playerIn, UpgCtileentityFluidTank tank, ItemStack equippedItemStack) {

        FluidStack fluidFromBucket = FluidContainerRegistry.getFluidForFilledItem(equippedItemStack);

        if (tank.fill(null, fluidFromBucket, false) == FluidContainerRegistry.BUCKET_VOLUME) {

            tank.fill(null, fluidFromBucket, true);

            if (!playerIn.capabilities.isCreativeMode) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
            }

        }

    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

        BlockPos up = pos.up();
        BlockPos down = pos.down();

        return (!(WorldHelper.getBlock(worldIn, up) == this && WorldHelper.getBlock(worldIn, up.up()) == this) && !(WorldHelper.getBlock(worldIn, down) == this && WorldHelper.getBlock(worldIn, down.down()) == this) && !(WorldHelper.getBlock(worldIn, up) == this && WorldHelper.getBlock(worldIn, down) == this)) && worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);

    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(TYPE)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return damageDropped(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, TankType.getType(meta));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (this.hasSubBlocks) {
            list.add(new ItemStack(this, 1, TankType.SOLID.getMeta()));
            list.add(new ItemStack(this, 1, TankType.GLASSES.getMeta()));
        } else
            list.add(new ItemStack(itemIn, 1, 0));
    }

    public enum TankType implements IStringSerializable {
        SOLID("solid", 0),
        GLASSES("glasses", 1);

        private final String name;
        private final int meta;

        TankType(String name, int meta) {
            this.name = name;
            this.meta = meta;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return this.meta;
        }

        public static TankType getType(int meta) {

            if (meta == 0)
                return SOLID;
            else
                return GLASSES;
        }
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {

        UpgCtileentityFluidTank tank = (UpgCtileentityFluidTank) worldIn.getTileEntity(pos);
        int capacity = tank.getCapacity();
        int fluidAmount = tank.getFluidAmount();

        int comparator = (int) (tank.getFillPercentage() * 15.0f);

        //In this way the output is max only if is tank(s) is (are) totally full
        if (comparator == 15 && fluidAmount < capacity)
            comparator--;

        return comparator;

    }

    /*@Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof UpgCtileentityFluidTank)
            return ((UpgCtileentityFluidTank) te).getFluidLight();
        else return super.getLightValue(state, world, pos);
    }*/

    @Override
    public void appliedConfig(SystemConfig.ConfigValue... values) {

        if (values.length >= 1) {
            for (SystemConfig.ConfigValue c : values) {

                if (c.configName.equals("basicTankCapacity")) {
                    this.capacity = new Integer(c.value);

                }
            }
        }
    }

}
