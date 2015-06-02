package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.UUID;

public class ItemUpgCPersonalInformation extends ItemUpgC {

    private static final String playerUUID = Reference.MOD_ID + ":playerUUID";

    public ItemUpgCPersonalInformation() {
        super();
        this.maxStackSize = 1;
        this.setUnlocalizedName(Names.Items.PERSONAL_INFORMATION);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.stackTagCompound = new NBTTagCompound();
    }

    private void setPlayer(ItemStack itemStack, EntityPlayer player) {
        UUID id = player.getUniqueID();

        String aaa = player.getCommandSenderName();

        if (itemStack.stackTagCompound == null)
            itemStack.stackTagCompound = new NBTTagCompound();

        itemStack.stackTagCompound.setString(playerUUID, id.toString());
    }

    public EntityPlayer getPlayer(ItemStack itemStack, World world) {

        if (itemStack.stackTagCompound != null) {
            if (itemStack.stackTagCompound.hasKey(playerUUID)) {
                String pUUID = itemStack.stackTagCompound.getString(playerUUID);
                EntityPlayer player = world.func_152378_a(UUID.fromString(pUUID));
                if (player != null) {
                    return player;
                }

            }
        }

        return null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        if (!world.isRemote)
            if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                setPlayer(itemStack, player);
            }

        return itemStack;

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {

        World world = entityPlayer.getEntityWorld();
        String playerName = "Unknow";
        EntityPlayer player = getPlayer(itemStack, world);

        if (player != null) {
            playerName = player.getDisplayName();

        }

        list.add(EnumChatFormatting.BLUE + "Player: " + EnumChatFormatting.RED + playerName);

    }
}
