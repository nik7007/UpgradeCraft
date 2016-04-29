package com.nik7.upgcraft.tileentities;


import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;

public class TestTE extends TileEntity implements ITickable {

    private int l = 0;
    private int tick = 0;
    private boolean up = true;

    public int getLight() {
        worldObj.notifyLightSet(pos);
        return l;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("l", l);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        l = compound.getInteger("l");
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        SPacketUpdateTileEntity buff = new SPacketUpdateTileEntity(pos, 0, tag);
        return buff;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        IBlockState blockState = worldObj.getBlockState(pos);
        if (blockState != null) {
            worldObj.notifyBlockUpdate(pos, blockState, blockState, 3);
            worldObj.setBlockState(pos, blockState, 3);
           /* Chunk chunk = worldObj.getChunkFromChunkCoords(pos.getX(), pos.getY());
            //chunk.setBlockState(pos,blockState);
            chunk.setLightFor(EnumSkyBlock.BLOCK, pos, l);
            chunk.checkLight();*/
        }
       /* worldObj.notifyNeighborsOfStateChange(pos, blockType);
        worldObj.notifyBlockOfStateChange(pos, blockType);*/
        /*BlockPos minPos = pos.add(-20, -20, -20);
        BlockPos maxPos = pos.add(20, 20, 20);
        worldObj.markBlockRangeForRenderUpdate(minPos, maxPos);*/

    }

    @Override
    public void onLoad() {
        //if (worldObj.isRemote) {
        BlockPos minPos = pos.add(-20, -20, -20);
        BlockPos maxPos = pos.add(20, 20, 20);
        worldObj.markBlockRangeForRenderUpdate(minPos, maxPos);
        //}
    }


    @Override
    public void update() {
        if (tick == 0) {
            if (up) {
                if (l < 15)
                    l++;
                else up = false;
            } else {
                if (l > 0)
                    l--;
                else up = true;
            }
            IBlockState blockState = worldObj.getBlockState(pos);
            if (blockState != null)
                worldObj.notifyBlockUpdate(pos, blockState, blockState, 3);

            BlockPos minPos = pos.add(-20, -20, -20);
            BlockPos maxPos = pos.add(20, 20, 20);
            worldObj.markBlockRangeForRenderUpdate(minPos, maxPos);

            markDirty();

        }
        tick++;
        tick %= 20 / 2;
    }
}
