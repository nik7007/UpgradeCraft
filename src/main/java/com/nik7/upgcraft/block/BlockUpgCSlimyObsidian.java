package com.nik7.upgcraft.block;

import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockUpgCSlimyObsidian extends BlockUpgC implements CustomCraftingExperience {

    public BlockUpgCSlimyObsidian() {
        super(Material.rock, "SlimyObsidian");
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
        this.slipperiness = 1.08F;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.IGNORE;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 1f;
    }

    public MapColor getMapColor(IBlockState state) {
        return MapColor.blackColor;
    }
}
