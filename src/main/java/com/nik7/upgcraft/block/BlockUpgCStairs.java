package com.nik7.upgcraft.block;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockUpgCStairs extends BlockStairs implements IBlockUpgC {

    private final String name;

    public BlockUpgCStairs(IBlockState modelState) {
        super(modelState);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        Block block = modelState.getBlock();
        if (block instanceof IBlockUpgC)
            name = "Stairs" + ((IBlockUpgC) block).getName();
        else {
            String unlocalizedName = getUnlocalizedName();
            String v[] = unlocalizedName.split(".");
            String wontedName = v[v.length - 2];
            wontedName = "Stairs" + wontedName;
            name = wontedName;
        }

        this.setUnlocalizedName(name);

    }

    public BlockUpgCStairs(IBlockState modelState, String name) {
        super(modelState);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.name = name;
        this.setUnlocalizedName(name);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getName() {
        return name;
    }
}
