package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.init.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityInventoryAndFluidHandler extends TileEntityFluidHandler implements ISidedInventory {

    protected final NonNullList<ItemStack> inventory;
    private String customName;

    public TileEntityInventoryAndFluidHandler(EnumCapacity capacity, int inventorySize) {
        this(capacity, null, inventorySize);

    }

    public TileEntityInventoryAndFluidHandler(EnumCapacity capacity, Class<? extends UpgCFluidTank> tankClass, int inventorySize) {
        super(capacity, tankClass);
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        ItemStackHelper.loadAllItems(tag, this.inventory);
        if (tag.hasKey("CustomName", 8)) {
            this.customName = tag.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);

        if (this.hasCustomName()) {
            tag.setString("CustomName", this.customName);
        }

        return tag;
    }


    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq((double) this.getPos().getX() + 0.5D, (double) this.getPos().getY() + 0.5D, (double) this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    @Nullable
    public String getName() {
        return this.hasCustomName() ? this.customName : null;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(ModBlocks.blockFluidInfuser.getUnlocalizedName() + ".name");
    }
}
