package com.nik7.upgcraft.client.renderer;

import com.nik7.upgcraft.init.IUpgC;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUpgC {

    public static void initRenderingAndTextures() {

    }

    private static ModelResourceLocation createLocation(IUpgC upgC) {
        return createLocation(upgC.getName());
    }

    private static ModelResourceLocation createLocation(String location) {
        return new ModelResourceLocation(location, "inventory");
    }

}
