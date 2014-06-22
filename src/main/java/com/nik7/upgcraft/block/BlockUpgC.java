package com.nik7.upgcraft.block;

import com.nik7.upgcraft.CreativeTab.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockUpgC extends Block {

    public BlockUpgC() {
        super(Material.rock);
    }

    public BlockUpgC(Material material) {
        super(material);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
    }


}
