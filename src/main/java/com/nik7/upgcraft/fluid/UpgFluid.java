package com.nik7.upgcraft.fluid;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraftforge.fluids.Fluid;

public class UpgFluid extends Fluid {

    public UpgFluid(String fluidName) {
        super(fluidName);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("fluid.%s%s", Names.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
}
