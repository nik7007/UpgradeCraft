package com.nik7.upgcraft.proxy;


import com.nik7.upgcraft.client.renderer.RenderUpgC;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {


    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        //texture
        RenderUpgC.initRenderingAndTextures();
    }


}
