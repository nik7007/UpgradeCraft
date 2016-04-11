package com.nik7.upgcraft.block;


import com.nik7.upgcraft.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class BlockUpgCFluidTankMold extends BlockUpgC {

    public static final PropertyEnum<BlockUpgCFluidTank.TankType> TYPE = PropertyEnum.create("type", BlockUpgCFluidTank.TankType.class);
    public static final PropertyBool COOKED = PropertyBool.create("cooked");
    public static final PropertyBool FULL = PropertyBool.create("full");

    public BlockUpgCFluidTankMold() {
        super(Material.clay, "FluidTankMold");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.SOLID).withProperty(COOKED, false).withProperty(FULL, false));
        this.setHardness(2.8f);

        this.setHarvestLevel("shovel", 0, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.GLASSES).withProperty(COOKED, false).withProperty(FULL, false));
        this.setHarvestLevel("shovel", 0, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.SOLID).withProperty(COOKED, false).withProperty(FULL, false));

        this.setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.GLASSES).withProperty(COOKED, true).withProperty(FULL, true));
        this.setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.SOLID).withProperty(COOKED, true).withProperty(FULL, true));
        this.setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.GLASSES).withProperty(COOKED, true).withProperty(FULL, false));
        this.setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(TYPE, BlockUpgCFluidTank.TankType.SOLID).withProperty(COOKED, true).withProperty(FULL, false));


    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, COOKED, FULL);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {

        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 4));
        list.add(new ItemStack(this, 1, 5));
        list.add(new ItemStack(this, 1, 6));
        list.add(new ItemStack(this, 1, 7));

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return damageDropped(state);
    }

    @Override
    public int damageDropped(IBlockState state) {
        BlockUpgCFluidTank.TankType type = state.getValue(TYPE);
        boolean cooked = state.getValue(COOKED);
        boolean full = state.getValue(FULL);

        int meta = type.getMeta();

        if (full) {
            meta += 6;
        } else if (cooked)
            meta += 4;

        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        BlockUpgCFluidTank.TankType type = BlockUpgCFluidTank.TankType.SOLID;
        boolean cooked = false;
        boolean full = false;

        if ((meta & 1) == 1)
            type = BlockUpgCFluidTank.TankType.GLASSES;
        if (((meta >> 1) & 1) >= 1) {
            cooked = true;
            full = true;
        } else if ((meta >> 2) >= 1) {
            cooked = true;
        }

        return this.getDefaultState().withProperty(TYPE, type).withProperty(COOKED, cooked).withProperty(FULL, full);

    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> res = new LinkedList<>();

        BlockUpgCFluidTank.TankType type = state.getValue(TYPE);
        boolean cooked = state.getValue(COOKED);
        boolean full = state.getValue(FULL);

        if (!cooked) {
            res.add(new ItemStack(this, 1, type.getMeta()));
        } else if (full) {
            res.add(new ItemStack(ModBlocks.blockUpgCIronFluidTank, 1, type.getMeta()));
        }

        return res;
    }


}
