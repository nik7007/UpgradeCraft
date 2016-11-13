package com.nik7.upgcraft.proxy;


import com.nik7.upgcraft.init.IUpgC;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ClientProxy extends CommonProxy {

    @Override
    public void initRenderingAndTextures() {

    }

    private ModelResourceLocation createLocation(IUpgC upgC) {
        return createLocation(upgC.getName());
    }

    private ModelResourceLocation createLocation(String location) {
        return new ModelResourceLocation(location, "inventory");
    }
}
