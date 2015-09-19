package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class SlotInfuser extends Slot {

    private int itemN;
    private EntityPlayer thePlayer;

    public SlotInfuser(EntityPlayer player, IInventory entity, int slotN, int x, int y) {
        super(entity, slotN, x, y);
        this.thePlayer = player;

    }

    public boolean isItemValid(ItemStack itemStack) {

        return false;
    }

    public ItemStack decrStackSize(int size) {
        if (this.getHasStack()) {
            this.itemN += Math.min(size, this.getStack().stackSize);
        }

        return super.decrStackSize(size);
    }

    public void onPickupFromSlot(EntityPlayer player, ItemStack item) {
        this.onCrafting(item);
        super.onPickupFromSlot(player, item);
    }

    protected void onCrafting(ItemStack item, int size) {
        this.itemN += size;
        this.onCrafting(item);
    }

    protected void onCrafting(ItemStack item) {

        item.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.itemN);

        if (!this.thePlayer.worldObj.isRemote) {
            int i = this.itemN;
            float f = 0;
            int j;


            Item itemI = item.getItem();

            if (itemI instanceof CustomCraftingExperience) {
                f = ((CustomCraftingExperience) item.getItem()).getCustomCraftingExperience(item);
            } else if (itemI instanceof ItemBlock) {
                Block block = ((ItemBlock) itemI).field_150939_a;
                if (block instanceof CustomCraftingExperience)
                    f = ((CustomCraftingExperience) block).getCustomCraftingExperience(item);
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

        this.itemN = 0;

    }

}
