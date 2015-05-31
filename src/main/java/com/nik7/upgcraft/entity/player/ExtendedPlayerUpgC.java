package com.nik7.upgcraft.entity.player;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayerUpgC implements IExtendedEntityProperties {

    private final EntityPlayer player;
    private UpgCTank UpgCEnderTank;

    public ExtendedPlayerUpgC(EntityPlayer player) {
        this.player = player;
        UpgCEnderTank = new UpgCTank(Capacity.SMALL_TANK);
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(Reference.EXTENDED_PLAYER, new ExtendedPlayerUpgC(player));
    }

    public static ExtendedPlayerUpgC get(EntityPlayer player) {
        return (ExtendedPlayerUpgC) player.getExtendedProperties(Reference.EXTENDED_PLAYER);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {

        NBTTagCompound properties = new NBTTagCompound();
        UpgCEnderTank.writeToNBT(properties);
        compound.setTag(Reference.EXTENDED_PLAYER, properties);

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(Reference.EXTENDED_PLAYER);
        UpgCEnderTank.readFromNBT(properties);

    }

    public static UpgCTank getUpgCEnderTank(EntityPlayer player) {

        return ((ExtendedPlayerUpgC) player.getExtendedProperties(Reference.EXTENDED_PLAYER)).UpgCEnderTank;

    }

    @Override
    public void init(Entity entity, World world) {

    }
}
