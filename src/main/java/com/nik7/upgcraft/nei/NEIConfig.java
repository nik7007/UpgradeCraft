package com.nik7.upgcraft.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.nik7.upgcraft.reference.Reference;

public class NEIConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        FluidInfuserRecipeHandler fluidInfuserRecipeHandler = new FluidInfuserRecipeHandler();
        API.registerRecipeHandler(fluidInfuserRecipeHandler);
        API.registerUsageHandler(fluidInfuserRecipeHandler);

    }

    @Override
    public String getName() {
        return Reference.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Reference.VERSION;
    }
}
