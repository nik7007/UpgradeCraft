package com.nik7.upgcraft.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateRequestMessage implements IMessage {

    private int x, y, z;

    public UpdateRequestMessage() {
    }

    public UpdateRequestMessage(BlockPos pos) {

        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

}
