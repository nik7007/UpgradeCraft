package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class FluidTank extends BlockUpgC implements ITileEntityProvider {

    private static final AxisAlignedBB BB = new AxisAlignedBB(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
    public static final PropertyBool GLASSED = PropertyBool.create("glassed");
    public static final PropertyEnum<RenderInformation> RENDER_INFORMATION = PropertyEnum.create("render_information", RenderInformation.class);

    public FluidTank() {
        super(Material.WOOD, "fluidtank");
        this.setDefaultState(this.blockState.getBaseState().withProperty(GLASSED, false).withProperty(RENDER_INFORMATION, RenderInformation.SINGLE));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

        BlockPos up = pos.up();
        BlockPos down = pos.down();

        return (!(WorldHelper.getBlock(worldIn, up) == this && WorldHelper.getBlock(worldIn, up.up()) == this) && !(WorldHelper.getBlock(worldIn, down) == this && WorldHelper.getBlock(worldIn, down.down()) == this) && !(WorldHelper.getBlock(worldIn, up) == this && WorldHelper.getBlock(worldIn, down) == this)) && worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);

    }

    private boolean isDouble(IBlockAccess world, BlockPos pos) {

        return world.getBlockState(pos.up()).getBlock() == this || world.getBlockState(pos.down()).getBlock() == this;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

        boolean isDouble = isDouble(world, pos);
        boolean isTop;
        boolean equal;
        boolean isGLASSED = state.getValue(GLASSED);
        RenderInformation information = RenderInformation.getRenderInformation(isGLASSED);

        if (isDouble) {
            isTop = WorldHelper.getBlock(world,pos) == this;
            if (isTop) {
                equal = isGLASSED == world.getBlockState(pos.down()).getValue(GLASSED);
            } else
                equal = isGLASSED == world.getBlockState(pos.up()).getValue(GLASSED);
            information = RenderInformation.getRenderInformation(true, isTop, isGLASSED, equal);
        }

        return state.withProperty(RENDER_INFORMATION, information);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {

        ItemStack mainHandItem = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        if (mainHandItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || offHandItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            if (world != null && !world.isRemote) {
                FluidActionResult result;
                IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, pos, null);

                result = interactWithFluidHandler(EntityEquipmentSlot.MAINHAND, mainHandItem, fluidHandler, player);
                ItemStack equippedItemStack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                if (equippedItemStack.getItem() instanceof ItemAir && !result.isSuccess())
                    interactWithFluidHandler(EntityEquipmentSlot.OFFHAND, offHandItem, fluidHandler, player);
            }
            return true;
        }
        return false;

    }

    private FluidActionResult interactWithFluidHandler(EntityEquipmentSlot slotIn, ItemStack equippedItemStack, IFluidHandler fluidHandler, EntityPlayer player) {
        FluidActionResult result = FluidUtil.interactWithFluidHandler(equippedItemStack, fluidHandler, player);
        if (result.isSuccess())
            player.setItemStackToSlot(slotIn, result.getResult());
        return result;
    }


    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GLASSED, RENDER_INFORMATION);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(GLASSED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(GLASSED) ? 1 : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityFluidTank) {
            TileEntityFluidTank fluidTank = (TileEntityFluidTank) te;
            float percentage = fluidTank.getFillPercentage();

            int result = (int) (percentage * 15f);

            if (percentage == 1)
                result = 15;
            else if (result == 15)
                result = 14;
            if (result == 0 && percentage > 0)
                result = 1;

            return result;

        }
        return 0;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

        if (state.getValue(GLASSED)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityFluidTank)
                return ((TileEntityFluidTank) te).getFluidLight();
        }
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFluidTank();
    }

    public enum RenderInformation implements IStringSerializable {
        SINGLE("single"),
        SINGLE_GLASSED("single_glassed"),
        DOUBLE_UP("double_up"),
        DOUBLE_DOWN("double_down"),
        DOUBLE_GASSED_UP("double_glassed_up"),
        DOUBLE_GASSED_DOWN("double_glassed_down"),
        DOUBLE_GASSED_UP_P("double_glassed_up_p"),
        DOUBLE_GASSED_DOWN_P("double_glassed_down_p");


        private final String name;

        public static RenderInformation getRenderInformation(boolean isGlassed) {
            return getRenderInformation(false, false, isGlassed, false);
        }

        public static RenderInformation getRenderInformation(boolean isDouble, boolean isTop, boolean isGlassed, boolean isEqual) {

            if (isDouble) {
                if (isTop) {
                    if (isGlassed) {
                        if (isEqual) {
                            return DOUBLE_GASSED_UP;
                        }
                        return DOUBLE_GASSED_UP_P;
                    }
                    return DOUBLE_UP;
                } else {
                    if (isGlassed) {
                        if (isEqual) {
                            return DOUBLE_GASSED_DOWN;
                        }
                        return DOUBLE_GASSED_DOWN_P;
                    }
                    return DOUBLE_DOWN;
                }
            } else if (isGlassed)
                return SINGLE_GLASSED;
            return SINGLE;
        }

        RenderInformation(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

}
