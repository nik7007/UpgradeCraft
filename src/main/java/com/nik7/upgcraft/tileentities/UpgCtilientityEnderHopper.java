package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.item.ItemUpgCPersonalInformation;
import com.nik7.upgcraft.network.DescriptionHandler;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.util.ItemHelper;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class UpgCtilientityEnderHopper extends TileEntity implements IHopper, ISidedInventory {

    private ItemStack inventory[] = new ItemStack[8]; //slot: 0 {up personal information} - slot: 1 {fuel} - slots:[2-6] {inventory} - slot: 7 {exit personal information}
    private String customName = Names.Inventory.UPGC_ENDER_HOPPER;
    private int cycle = 0;

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            tag.setString("CustomName", this.customName);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length) {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        if (tag.hasKey("CustomName")) {
            this.customName = tag.getString("CustomName");
        }

    }

    @Override
    public Packet getDescriptionPacket() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(xCoord);
        buf.writeInt(yCoord);
        buf.writeInt(zCoord);
        writeToPacket(buf);
        return new FMLProxyPacket(buf, DescriptionHandler.CHANNEL);
    }

    public void writeToPacket(ByteBuf buf) {

    }

    public void readFromPacket(ByteBuf buf) {

    }


    @Override
    public void updateEntity() {

        if (!worldObj.isRemote) {
            cycle++;
            if ((cycle %= 7) == 0)
                getItemStackFromTop();
        }
    }

    void getItemStackFromTop() {

        IInventory inventory = null;


        if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) instanceof BlockEnderChest) {

            if (this.inventory[0] != null && this.inventory[0].getItem() instanceof ItemUpgCPersonalInformation) {
                EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(this.inventory[0], getWorldObj());

                if (player != null) {
                    inventory = getEnderChestInventory(this.inventory[0]);
                }

            }

        } else if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IInventory) {

            inventory = (IInventory) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);

        } else if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof ISidedInventory) {
            inventory = (ISidedInventory) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        }
        if (inventory != null)
            transferItems(inventory, this, 1, 0, 0);
        else {
            EntityItem entityItem = getItemsFromUpAir(getWorldObj(), getXPos(), getYPos() + 1, getZPos());

            if (entityItem != null) {

                ItemStack origin = entityItem.getEntityItem();
                int result = transferItemToInventory(this, origin, 1, 0);
                ItemStack itemStackResult = ItemHelper.generateItemStack(origin, origin.stackSize - result);

                if (itemStackResult != null && itemStackResult.stackSize > 0)
                    entityItem.setEntityItemStack(itemStackResult);
                else entityItem.setDead();
            }
        }


    }

    private static EntityItem getItemsFromUpAir(World world, double x, double y, double z) {
        List list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectAnything);
        return list.size() > 0 ? (EntityItem) list.get(0) : null;
    }

    private static void transferItems(IInventory src, IInventory dest, int quantity, int sideSrc, int sideDest) {

        if (src == null || dest == null)
            return;

        int indexSrc = -1;
        ItemStack itemStackSrc = null;

        if (src instanceof ISidedInventory) {
            int[] slots = ((ISidedInventory) src).getAccessibleSlotsFromSide(sideSrc);

            for (int i : slots) {
                if ((itemStackSrc = src.getStackInSlot(i)) != null) {
                    indexSrc = i;
                    break;
                }
            }

        } else {
            int dimSrc = src.getSizeInventory();
            for (int i = 0; i < dimSrc; i++) {
                if ((itemStackSrc = src.getStackInSlot(i)) != null) {
                    indexSrc = i;
                    break;
                }
            }
        }

        if (itemStackSrc != null) {

            src.decrStackSize(indexSrc, transferItemToInventory(dest, itemStackSrc, quantity, sideDest));

        }


    }

    private static int transferItemToInventory(IInventory inventory, ItemStack itemStack, int quantity, int side) {


        if (inventory == null || itemStack == null)
            return 0;

        int indexDest = -1;
        ItemStack imItemStackDest = null;

        if (inventory instanceof ISidedInventory) {
            int slots[] = ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side);

            for (int i : slots) {
                if (canPutItemsInventory(inventory, itemStack, i) && ((imItemStackDest = inventory.getStackInSlot(i)) == null || imItemStackDest.stackSize < imItemStackDest.getItem().getItemStackLimit(imItemStackDest))) {
                    indexDest = i;
                    break;
                }
            }

        } else {
            int dimDest = inventory.getSizeInventory();
            for (int i = 0; i < dimDest; i++) {
                if (canPutItemsInventory(inventory, itemStack, i) && ((imItemStackDest = inventory.getStackInSlot(i)) == null || imItemStackDest.stackSize < imItemStackDest.getItem().getItemStackLimit(imItemStackDest))) {
                    indexDest = i;
                    imItemStackDest = inventory.getStackInSlot(i);
                    break;
                }
            }
        }

        if (indexDest != -1) {

            quantity = Math.min(inventory.getInventoryStackLimit(), quantity);
            quantity = Math.min(quantity, itemStack.getItem().getItemStackLimit(itemStack));

            int quantityDest = quantity;

            if (imItemStackDest != null) {
                int max;
                if (imItemStackDest.stackSize + quantity > (max = imItemStackDest.getItem().getItemStackLimit(imItemStackDest))) {

                    quantity = max - imItemStackDest.stackSize;
                }

                int newDim = imItemStackDest.stackSize + quantity;
                if (newDim > inventory.getInventoryStackLimit()) {
                    quantityDest = quantity = quantity - (newDim - inventory.getInventoryStackLimit());

                } else quantityDest = newDim;

            }

            inventory.setInventorySlotContents(indexDest, ItemHelper.generateItemStack(itemStack, quantityDest));

            return quantity;


        }

        return 0;
    }

    private static boolean canPutItemsInventory(IInventory inventory, ItemStack itemStack, int slot) {

        if (inventory.isItemValidForSlot(slot, itemStack)) {
            ItemStack stack = inventory.getStackInSlot(slot);
            return stack == null || stack.stackSize < stack.getItem().getItemStackLimit(stack);
        } else return false;
    }


    private IInventory getEnderChestInventory(ItemStack personalInformation) {

        if (!(personalInformation.getItem() instanceof ItemUpgCPersonalInformation))
            return null;
        EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(personalInformation, this.getWorldObj());
        if (player == null)
            return null;
        return player.getInventoryEnderChest();

    }


    @Override
    public double getXPos() {
        return (double) this.xCoord;
    }

    @Override
    public double getYPos() {
        return (double) this.yCoord;
    }

    @Override
    public double getZPos() {
        return (double) this.zCoord;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < getSizeInventory()) {
            return inventory[slot];
        } else {
            LogHelper.error("Try to get Stack in slot that doesn't exist!!");
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int maxNumber) {
        if (maxNumber == 0 || slot >= getSizeInventory()) {
            return null;
        } else if (inventory[slot] == null) {
            return null;
        } else {
            int number;
            ItemStack itemStack = inventory[slot];
            if (itemStack.stackSize >= maxNumber) {
                number = maxNumber;
            } else number = itemStack.stackSize;

            inventory[slot].stackSize = itemStack.stackSize - number;

            if (inventory[slot].stackSize <= 0) {
                inventory[slot] = null;
            }

            ItemStack result = new ItemStack(itemStack.getItem(), number, itemStack.getItemDamage());

            if (itemStack.hasTagCompound())
                result.setTagCompound(itemStack.getTagCompound());

            return result;

        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (inventory[slot] != null) {
            ItemStack itemStack = inventory[slot];
            inventory[slot] = null;
            return itemStack;
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        this.inventory[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

    }

    @Override
    public String getInventoryName() {

        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_ENDER_HOPPER;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return !(slot < 0 || slot >= getSizeInventory()) && (inventory[slot] == null || itemStack == null || inventory[slot].isItemEqual(itemStack) && !((inventory[slot].hasTagCompound() && !itemStack.hasTagCompound()) || (!inventory[slot].hasTagCompound() && itemStack.hasTagCompound())) && (!(inventory[slot].hasTagCompound() && itemStack.hasTagCompound()) || inventory[slot].getTagCompound().equals(itemStack.hasTagCompound()))) && (inventory[slot] == null || inventory[slot].stackSize < inventory[slot].getItem().getItemStackLimit(inventory[slot]));

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{2, 3, 4, 5, 6};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
