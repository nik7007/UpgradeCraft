package com.nik7.upgcraft.fluid;

import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class UpgFluid extends Fluid {

    public UpgFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
    }


    @Override
    public String getUnlocalizedName() {
        return String.format("fluid.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
}
