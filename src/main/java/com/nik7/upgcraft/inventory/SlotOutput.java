package com.nik7.upgcraft.inventory;


import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotOutput extends Slot {

    private final EntityPlayer player;
    private int removeCount;

    public SlotOutput(EntityPlayer player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);

        if (!this.player.world.isRemote) {
            int count = this.removeCount;
            // TODO: add probability for items/blocks
            float expProbability = 1;

            if (expProbability == 0.0F) {
                count = 0;
            } else if (expProbability < 1.0F) {
                int j = MathHelper.floor((float) count * expProbability);

                if (j < MathHelper.ceil((float) count * expProbability) && Math.random() < (double) ((float) count * expProbability - (float) j)) {
                    ++j;
                }

                count = j;
            }

            while (count > 0) {
                int k = EntityXPOrb.getXPSplit(count);
                count -= k;
                this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
            }
        }

        this.removeCount = 0;


    }


}
