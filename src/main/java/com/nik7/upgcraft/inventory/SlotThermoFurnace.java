package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.handler.AchievementHandler;
import com.nik7.upgcraft.registry.CustomCraftingExperience;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotThermoFurnace extends SlotFurnaceOutput {

    private int slotIndex;
    private EntityPlayer thePlayer;

    public SlotThermoFurnace(EntityPlayer player, IInventory inventory, int slotIndex, int x, int y) {
        super(player, inventory, slotIndex, x, y);
        this.slotIndex = slotIndex;
        this.thePlayer = player;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
        AchievementHandler.craftAchievement(playerIn, stack);
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {

        super.onCrafting(itemStack);

        if (!this.thePlayer.worldObj.isRemote) {
            int i = this.slotIndex;
            float f = 0;
            int j;


            Item itemI = itemStack.getItem();

            if (itemI instanceof CustomCraftingExperience) {
                f = ((CustomCraftingExperience) itemStack.getItem()).getCustomCraftingExperience(itemStack);
            } else if (itemI instanceof ItemBlock) {
                Block block = ((ItemBlock) itemI).block;
                if (block instanceof CustomCraftingExperience)
                    f = ((CustomCraftingExperience) block).getCustomCraftingExperience(itemStack);
            }

            if (f == 0.0F) {
                i = 0;
            } else if (f < 1.0F) {
                j = MathHelper.floor_float((float) i * f);

                if (j < MathHelper.ceiling_float_int((float) i * f) && (float) Math.random() < (float) i * f - (float) j) {
                    ++j;
                }

                i = j;
            }

            while (i > 0) {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, j));
            }
        }

        this.slotIndex = 0;

    }
}
