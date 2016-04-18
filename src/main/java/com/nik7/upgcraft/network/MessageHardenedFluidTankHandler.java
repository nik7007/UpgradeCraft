package com.nik7.upgcraft.network;


import com.nik7.upgcraft.block.BlockUpgCClayFluidTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHardenedFluidTankHandler implements IMessageHandler<UpdateRequestMessage, UpdateRequestMessage> {


    @Override
    public UpdateRequestMessage onMessage(final UpdateRequestMessage message, MessageContext ctx) {

        if (ctx.side == Side.CLIENT) {
            IThreadListener mainThread = Minecraft.getMinecraft();

            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World clientWorld = Minecraft.getMinecraft().theWorld;
                    BlockPos pos = message.getBlockPos();
                    Block block = WorldHelper.getBlock(clientWorld, pos);
                    if (block == ModBlocks.blockUpgCClayFluidTank) {
                        IBlockState state = clientWorld.getBlockState(pos);
                        ((BlockUpgCClayFluidTank) block).hardenedClayTank(clientWorld, pos, state);
                    }

                }
            });


        }

        return null;
    }
}
