package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemUpgCPersonalInformation extends ItemUpgC {

    private static final String playerUUID = Reference.MOD_ID + ":playerUUID";
    private static final String playerName = Reference.MOD_ID + ":playerName";
    private static final Random random = new Random();
    private static String oldRnDString = "No player";

    public ItemUpgCPersonalInformation() {
        super();
        this.maxStackSize = 1;
        this.setUnlocalizedName(Names.Items.PERSONAL_INFORMATION);
        this.setTextureName(Texture.Items.PERSONAL_INFORMATION);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.stackTagCompound = new NBTTagCompound();
    }

    private void setPlayer(ItemStack itemStack, EntityPlayer player) {
        UUID id = player.getUniqueID();

        String name = player.getCommandSenderName();

        if (itemStack.stackTagCompound == null)
            itemStack.stackTagCompound = new NBTTagCompound();

        itemStack.stackTagCompound.setString(playerUUID, id.toString());
        itemStack.stackTagCompound.setString(playerName, name);
    }

    public static EntityPlayer getPlayer(ItemStack itemStack, World world) {

        if (itemStack != null && itemStack.stackTagCompound != null) {

            if (itemStack.stackTagCompound.hasKey(playerUUID)) {
                String pUUID = itemStack.stackTagCompound.getString(playerUUID);
                EntityPlayer player = world.func_152378_a(UUID.fromString(pUUID));
                String name = itemStack.stackTagCompound.getString(playerName);
                if (player != null) {

                    if (!player.getCommandSenderName().equals(name)) {
                        itemStack.stackTagCompound.setString(playerName, player.getCommandSenderName());
                    }

                    return player;
                } else {
                    player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
                    if (player != null)
                        return player;
                }

            }
        }

        return null;
    }

    public boolean isPlayerSet(ItemStack itemStack) {
        return itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey(playerUUID);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        if (!world.isRemote)
            if (player.isSneaking()) {
                setPlayer(itemStack, player);
            }

        return itemStack;

    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int pass) {
        return isPlayerSet(itemStack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {

        World world = entityPlayer.getEntityWorld();
        String playerName = "????";
        EntityPlayer player = getPlayer(itemStack, world);

        if (player != null) {
            playerName = player.getDisplayName();
        } else if (isPlayerSet(itemStack)) {

            int ln = 4 + (int) (Math.random() * 5);
            char s[] = new char[ln];
            String newName = new BigInteger(130, random).toString(32);
            String rndName = oldRnDString;

            if (Math.random() > 0.63) {
                for (int i = 0; i < ln; i++)
                    s[i] = newName.charAt(i);

                rndName = new String(s);
                oldRnDString = rndName;
            }

            playerName = rndName;

        }

        playerName = ((playerName.equals("????")) ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + playerName;

        list.add(EnumChatFormatting.GRAY + "[WIP]");
        list.add(EnumChatFormatting.BLUE + "Player: " + playerName);

    }
}
