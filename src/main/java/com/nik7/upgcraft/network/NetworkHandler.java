package com.nik7.upgcraft.network;


import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class NetworkHandler {
    private static SimpleNetworkWrapper INSTANCE;
    private static int ID = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        NetworkHandler.registerMessage(new MessageUpdateRequestHandler(), UpdateRequestMessage.class, Side.SERVER);
        NetworkHandler.registerMessage(new MessageHardenedFluidTankHandler(), UpdateRequestMessage.class, Side.CLIENT);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestMessageType, Side side) {
        INSTANCE.registerMessage(messageHandler, requestMessageType, ID++, side);
    }

    public static SimpleNetworkWrapper getInstance() {
        return INSTANCE;
    }


}
