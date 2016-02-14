package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new ContainerFluidFurnace(player.inventory, (UpgCtileentityFluidFurnace) world.getTileEntity(blockPos));
            case FLUID_INFUSER:
                return new ContainerFluidInfuser(player.inventory, (UpgCtileentityFluidInfuser) world.getTileEntity(blockPos));

        }
        throw new IllegalArgumentException("No gui with id " + ID);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new GuiFluidFurnace(player.inventory, (UpgCtileentityFluidFurnace) world.getTileEntity(blockPos));
            case FLUID_INFUSER:
                return new GuiFluidInfuser(player.inventory, (UpgCtileentityFluidInfuser) world.getTileEntity(blockPos));

        }
        throw new IllegalArgumentException("No gui with id " + ID);
    }
}
