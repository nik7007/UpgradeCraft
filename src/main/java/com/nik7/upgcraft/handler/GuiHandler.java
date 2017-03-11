package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);

        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new ContainerFluidFurnace(player.inventory, (TileEntityFluidFurnace) world.getTileEntity(blockPos));
            case FLUID_INFUSER:
                return new ContainerFluidInfuser(player.inventory, (TileEntityFluidInfuser) world.getTileEntity(blockPos));

        }

        throw new IllegalArgumentException("No gui with id " + ID);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        BlockPos blockPos = new BlockPos(x, y, z);

        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new GuiFluidFurnace(player.inventory, (TileEntityFluidFurnace) world.getTileEntity(blockPos));
        }

        throw new IllegalArgumentException("No gui with id " + ID);
    }
}
