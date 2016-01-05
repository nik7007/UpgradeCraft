package com.nik7.upgcraft.block;

import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockUpgC extends Block {

    public BlockUpgC() {
        super(Material.rock);
    }

    public BlockUpgC(Material material) {
        super(material);
       this.setCreativeTab(CreativeTab.UPGC_TAB);

    }

    public abstract String getName();

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }


}
