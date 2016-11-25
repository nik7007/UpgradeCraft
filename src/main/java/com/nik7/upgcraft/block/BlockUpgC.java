package com.nik7.upgcraft.block;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockUpgC extends Block {

    public BlockUpgC(Material blockMaterial, String blockName) {
        super(blockMaterial);
        setUnlocalizedName(Reference.MOD_ID + "." + blockName);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.setRegistryName(blockName);

    }

}
