package com.nik7.upgcraft.block;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockUpgCClayBrick extends BlockUpgC {

    public static final PropertyBool IS_COOKED = PropertyBool.create("iscooked");
    public static final PropertyBool IS_SQUARED = PropertyBool.create("issquared");

    public BlockUpgCClayBrick() {
        super(Material.clay, "ClayBrick");
        setHardness(3.0F);
        setResistance(12.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(IS_COOKED, false).withProperty(IS_SQUARED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_COOKED, IS_SQUARED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return damageDropped(state);
    }

    @Override
    public int damageDropped(IBlockState state) {

        int meta = 0;
        if (state.getValue(IS_COOKED))
            meta++;
        if (state.getValue(IS_SQUARED))
            meta += 2;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(IS_COOKED, (meta % 2) != 0).withProperty(IS_SQUARED, meta >= 2);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
        list.add(new ItemStack(this, 1, 3));
    }
}
