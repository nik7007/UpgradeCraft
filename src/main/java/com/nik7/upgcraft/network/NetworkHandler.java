package com.nik7.upgcraft.network;


import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class NetworkHandler {
    private static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        INSTANCE.registerMessage(new MessageUpdateRequestHandler(),UpdateRequestMessage.class,0, Side.SERVER);
    }

    public static SimpleNetworkWrapper getInstance() {
        return INSTANCE;
    }


}
