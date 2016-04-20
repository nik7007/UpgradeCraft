package com.nik7.upgcraft.handler;


import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AchievementEventHandler {

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.player != null) {
            AchievementHandler.craftAchievement(event.player, event.crafting);
        }
    }

    @SubscribeEvent
    public void onPlayerPickup(EntityItemPickupEvent event) {
        if (event.getItem() != null && event.getEntityPlayer() != null) {
            AchievementHandler.pickupAchievement(event.getEntityPlayer(), event.getItem().getEntityItem());
        }
    }

}
