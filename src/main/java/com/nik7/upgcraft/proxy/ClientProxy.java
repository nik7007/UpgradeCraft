package com.nik7.upgcraft.proxy;


import com.nik7.upgcraft.client.renderer.RenderUpgC;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderUpgC.initRenderingAndTextures();
    }

    @Override
    public void init(FMLInitializationEvent event) {

        super.init(event);
        RenderUpgC.registerTileEntitySpecialRender();

    }


}
