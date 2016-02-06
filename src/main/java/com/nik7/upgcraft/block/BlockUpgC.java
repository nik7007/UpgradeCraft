package com.nik7.upgcraft.block;

import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockUpgC extends Block {

    protected final String name;

    public BlockUpgC(String name) {
        super(Material.rock);
        this.name = name;
        this.setUnlocalizedName(name);
    }

    public BlockUpgC(Material material, String name) {
        super(material);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.name = name;
        this.setUnlocalizedName(name);

    }

    public String getName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }


}
