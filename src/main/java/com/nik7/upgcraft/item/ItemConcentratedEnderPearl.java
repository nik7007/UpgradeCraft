package com.nik7.upgcraft.item;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemConcentratedEnderPearl extends ItemUpgC {


    private final int maxEnderPower = 128; // 8 ender pearl stack

    private final String namePower = "enderPower";

    public ItemConcentratedEnderPearl() {
        super();
        this.maxStackSize = 1;
        this.setUnlocalizedName(Names.Items.CONCENTRATED_ENDER_PEARL);
        this.setTextureName(Texture.Items.CONCENTRATED_ENDER_PEARL);

    }


    public int getEnderPower(ItemStack itemStack) {
        if (itemStack.stackTagCompound == null) {
            return 0;
        }
        return itemStack.stackTagCompound.getInteger(namePower);
    }

    public void setEnderPower(ItemStack itemStack, int enderPower) {
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        itemStack.stackTagCompound.setInteger(namePower, enderPower);
    }

    public boolean reduceEnderPower(int amount, ItemStack itemStack, EntityPlayer player) {

        if (getEnderPower(itemStack) == 0 && !player.capabilities.isCreativeMode) {
            return false;
        }
        if (!player.capabilities.isCreativeMode) {
            int enderPower = getEnderPower(itemStack);
            setEnderPower(itemStack, enderPower - amount);
            player.onUpdate();

        }
        return true;
    }

    public boolean increaseEnderPower(int amount, ItemStack itemStack) {
        if (getEnderPower(itemStack) == getMaxEnderPower()) {
            return false;
        }

        int enderPower = getEnderPower(itemStack);
        setEnderPower(itemStack, enderPower + amount);

        return true;
    }


    public int getMaxEnderPower() {
        return maxEnderPower;
    }


    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.stackTagCompound = new NBTTagCompound();
    }


    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        list.add("[WIP]");
        list.add(EnumChatFormatting.GREEN + "Ender Power: " + getEnderPower(itemStack) + "/" + getMaxEnderPower());
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        float enderPower = getEnderPower(itemStack);

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

            onItemShiftRightClick(player, itemStack);

        } else if (enderPower > 0 || player.capabilities.isCreativeMode) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityEnderPearl(world, player));
                reduceEnderPower(1, itemStack, player);
            }
        } else if (enderPower == 0) {
            LogHelper.info("out of ender Power");
        }

        return itemStack;

    }

    protected void onItemShiftRightClick(EntityPlayer player, ItemStack itemStackO) {

        float enderPower = getEnderPower(itemStackO);

        if (!player.capabilities.isCreativeMode) {
            if (enderPower < maxEnderPower) {

                ItemStack itemStack;

                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    itemStack = player.inventory.getStackInSlot(i);

                    if (itemStack != null) {

                        if (itemStack.getItem() == Items.ender_pearl) {

                            boolean flag = itemStack.stackSize == 1;
                            --itemStack.stackSize;

                            if (flag) {
                                player.inventory.setInventorySlotContents(i, null);
                            }
                            increaseEnderPower(1, itemStackO);
                            break;
                        }
                    }
                }


            }

        } else {
            setEnderPower(itemStackO, getMaxEnderPower());
        }
    }

}

