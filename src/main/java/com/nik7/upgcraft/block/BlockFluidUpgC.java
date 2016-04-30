package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public abstract class BlockFluidUpgC extends BlockFluidClassic implements IBlockUpgC {

    protected final String name;

    public BlockFluidUpgC(Fluid fluid, Material material, String name) {
        super(fluid, material);
        this.name = name;
        this.setUnlocalizedName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
}
