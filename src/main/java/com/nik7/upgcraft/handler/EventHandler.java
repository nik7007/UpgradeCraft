package com.nik7.upgcraft.handler;


import net.minecraftforge.common.MinecraftForge;

public class EventHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new AchievementEventHandler());
    }
}
