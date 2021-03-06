package com.nik7.upgcraft.network;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@ChannelHandler.Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {

    public static final String CHANNEL = Reference.MOD_ID + "Description";

    public static void init() {
        NetworkRegistry.INSTANCE.newChannel(CHANNEL, new DescriptionHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {

        PacketBuffer buf = (PacketBuffer) msg.payload();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();

        World clientWorld = UpgradeCraft.proxy.getClientPlayer().worldObj;
        BlockPos pos = new BlockPos(x, y, z);

        if (clientWorld.isBlockLoaded(pos)) {

            TileEntity te = clientWorld.getTileEntity(pos);

            if (te instanceof UpgCtileentityInventoryFluidHandler) {
                ((UpgCtileentityInventoryFluidHandler) te).readFromPacket(buf);
            }/* else if (te instanceof UpgCtilientityEnderHopper) {
            ((UpgCtilientityEnderHopper) te).readFromPacket(buf);
        }*/ else if (te == null) {
                NetworkHandler.getInstance().sendToServer(new UpdateRequestMessage(pos));
            }

        }

    }
}
