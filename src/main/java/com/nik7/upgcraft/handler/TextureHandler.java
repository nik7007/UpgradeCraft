package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureHandler {

    @SubscribeEvent
    public void onRegisterTexture(TextureStitchEvent.Pre event){
        event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "blocks/ActiveLava_still"));
        event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "blocks/ActiveLava_flow"));

    }

}
