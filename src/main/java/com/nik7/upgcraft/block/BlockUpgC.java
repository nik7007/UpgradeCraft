package com.nik7.upgcraft.block;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.init.IUpgC;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockUpgC extends Block implements IUpgC {

    private final String blockName;

    public BlockUpgC(Material blockMaterial, String blockName) {
        super(blockMaterial);
        this.setUnlocalizedName(blockName);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.blockName = blockName;
    }

    @Override
    public String getName() {
        return blockName;
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

}
