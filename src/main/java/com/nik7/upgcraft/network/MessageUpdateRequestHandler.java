package com.nik7.upgcraft.network;


import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateRequestHandler implements IMessageHandler<UpdateRequestMessage, UpdateRequestMessage> {

    @Override
    public UpdateRequestMessage onMessage(UpdateRequestMessage message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            World serverWorld = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity te = serverWorld.getTileEntity(message.getBlockPos());
            if (te instanceof UpgCtileentityInventoryFluidHandler)
                ((UpgCtileentityInventoryFluidHandler) te).forceUpdate();
        }
        return null;
    }
}
