package com.nik7.upgcraft.block;

import com.nik7.upgcraft.CreativeTab.CreativeTab;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockUpgC extends Block {

    public BlockUpgC() {
        super(Material.rock);
    }

    public BlockUpgC(Material material) {
        super(material);
        this.setCreativeTab(CreativeTab.UPGC_TAB);

    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Names.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }




}
