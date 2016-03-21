package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.entity.player.CapabilitiesPlayerUpgC;
import com.nik7.upgcraft.entity.player.ICapabilitiesPlayerUpgC;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.concurrent.Callable;

public class CapabilityPlayerUpgCHandler {


    public static void register() {

        CapabilityManager.INSTANCE.register(ICapabilitiesPlayerUpgC.class, new Storage(), new Factory());

    }

    private static class Storage implements Capability.IStorage<ICapabilitiesPlayerUpgC> {

        @Override
        public NBTBase writeNBT(Capability<ICapabilitiesPlayerUpgC> capability, ICapabilitiesPlayerUpgC instance, EnumFacing side) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound = instance.getEnderFluidTank().writeToNBT(nbtTagCompound);
            return nbtTagCompound;
        }

        @Override
        public void readNBT(Capability<ICapabilitiesPlayerUpgC> capability, ICapabilitiesPlayerUpgC instance, EnumFacing side, NBTBase nbt) {

            instance.getEnderFluidTank().readFromNBT((NBTTagCompound) nbt);

        }
    }

    private static class Factory implements Callable<ICapabilitiesPlayerUpgC> {

        @Override
        public ICapabilitiesPlayerUpgC call() throws Exception {
            return new CapabilitiesPlayerUpgC();
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable {

        @CapabilityInject(ICapabilitiesPlayerUpgC.class)
        public static Capability<ICapabilitiesPlayerUpgC> PLAYER_UPGC = null;

        private ICapabilitiesPlayerUpgC capabilitiesPlayerUpgC = PLAYER_UPGC.getDefaultInstance();


        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == PLAYER_UPGC;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == PLAYER_UPGC ? (T) capabilitiesPlayerUpgC : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return PLAYER_UPGC.getStorage().writeNBT(PLAYER_UPGC,capabilitiesPlayerUpgC,null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            PLAYER_UPGC.getStorage().readNBT(PLAYER_UPGC,capabilitiesPlayerUpgC,null,nbt);
        }
    }

}
