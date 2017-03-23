package com.nik7.upgcraft.block;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockUpgCStairs extends BlockStairs {

    public BlockUpgCStairs(IBlockState modelState) {
        super(modelState);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        Block block = modelState.getBlock();

        String name = block.getUnlocalizedName();

        String v[] = name.split(":");
        String wontedName = v[v.length - 1];
        wontedName = "stairs" + wontedName;

        this.setUnlocalizedName(Reference.RESOURCE_PREFIX + wontedName);
        this.setRegistryName(wontedName);

    }

    public BlockUpgCStairs(IBlockState modelState, String name) {
        super(modelState);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        Block block = modelState.getBlock();
        String blockName = block.getUnlocalizedName();
        String v[] = blockName.split(":");
        String wontedName = v[v.length - 1];
        wontedName = name + wontedName;

        this.setUnlocalizedName(Reference.RESOURCE_PREFIX + wontedName);
        this.setRegistryName(wontedName);
    }

}
