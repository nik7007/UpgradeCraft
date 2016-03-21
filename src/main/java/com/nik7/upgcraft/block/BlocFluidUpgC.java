package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public abstract class BlocFluidUpgC extends BlockFluidClassic {

    protected final String name;

    public BlocFluidUpgC(Fluid fluid, Material material, String name) {
        super(fluid, material);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /*@Override
    public int getRenderType() {
        return 1;
    }*/

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
}
