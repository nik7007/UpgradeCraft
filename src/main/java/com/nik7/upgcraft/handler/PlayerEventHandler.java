package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.entity.player.ExtendedPlayerUpgC;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer && ExtendedPlayerUpgC.get((EntityPlayer) event.entity) == null)
            ExtendedPlayerUpgC.register((EntityPlayer) event.entity);
    }

    public static void init(){

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

}
