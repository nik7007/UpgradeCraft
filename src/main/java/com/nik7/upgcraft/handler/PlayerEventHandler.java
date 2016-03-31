package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCEnderFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nik7.upgcraft.handler.CapabilityPlayerUpgCHandler.Provider.PLAYER_UPGC;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onEntityConstruct(AttachCapabilitiesEvent.Entity entity) {

        if (entity.getEntity() instanceof EntityPlayer) {
            entity.addCapability(new ResourceLocation(Reference.MOD_ID + "playerCapabilities"), new CapabilityPlayerUpgCHandler.Provider());

        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerEvent.Clone e) {
        if (e.isWasDeath()) {
            EntityPlayer playerOriginal = e.getOriginal();
            EntityPlayer playerNew = e.getEntityPlayer();

            if (playerOriginal.hasCapability(PLAYER_UPGC, null) && playerNew.hasCapability(PLAYER_UPGC, null)) {

                FluidStack fluidStack = playerOriginal.getCapability(PLAYER_UPGC, null).getEnderFluidTank().getFluid();
                UpgCEnderFluidTank fluidTank = playerNew.getCapability(PLAYER_UPGC, null).getEnderFluidTank();
                fluidTank.fill(fluidStack, true);
            }

        }

    }

    public static void init() {

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

}
