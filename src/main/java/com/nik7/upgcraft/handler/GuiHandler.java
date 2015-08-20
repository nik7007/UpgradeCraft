package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.client.gui.inventory.GuiEnderHopper;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.client.gui.inventory.GuiTermoFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerEnderHopper;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.inventory.ContainerTermoFluidFurnace;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import com.nik7.upgcraft.tileentities.UpgCtileentityTermoFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new ContainerFluidFurnace(player.inventory, (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z));

            case FLUID_INFUSER:
                return new ContainerFluidInfuser(player.inventory, (UpgCtileentityFluidInfuser) world.getTileEntity(x, y, z));
            case ENDER_HOPPER:
                return new ContainerEnderHopper(player.inventory, (UpgCtilientityEnderHopper) world.getTileEntity(x, y, z));
            case TERMO_FLUID_FURNACE:
                return new ContainerTermoFluidFurnace(player.inventory, (UpgCtileentityTermoFluidFurnace) world.getTileEntity(x, y, z));
        }
        throw new IllegalArgumentException("No gui with id " + ID);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (GUIs.values()[ID]) {
            case FLUID_FURNACE:
                return new GuiFluidFurnace(player.inventory, (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z));
            case FLUID_INFUSER:
                return new GuiFluidInfuser(player.inventory, (UpgCtileentityFluidInfuser) world.getTileEntity(x, y, z));
            case ENDER_HOPPER:
                return new GuiEnderHopper(player.inventory, (UpgCtilientityEnderHopper) world.getTileEntity(x, y, z));
            case TERMO_FLUID_FURNACE:
                return new GuiTermoFluidFurnace(player.inventory, (UpgCtileentityTermoFluidFurnace) world.getTileEntity(x, y, z));
        }
        throw new IllegalArgumentException("No gui with id " + ID);
    }
}
