package com.nik7.upgcraft.network;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.Reference;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@ChannelHandler.Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {

    public static final String CHANNEL = Reference.MOD_ID + "Description";

    static {
        NetworkRegistry.INSTANCE.newChannel(CHANNEL, new DescriptionHandler());
    }

    public static void init() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {

        ByteBuf buf = msg.payload();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        TileEntity te = UpgradeCraft.proxy.getClientPlayer().worldObj.getTileEntity(new BlockPos(x,y,z));
       /* if (te instanceof UpgCtileentityInventoryFluidHandler) {
            ((UpgCtileentityInventoryFluidHandler) te).readFromPacket(buf);
        } else if (te instanceof UpgCtilientityEnderHopper) {
            ((UpgCtilientityEnderHopper) te).readFromPacket(buf);
        }*/

    }
}
