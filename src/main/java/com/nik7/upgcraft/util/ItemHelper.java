package com.nik7.upgcraft.util;


import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemHelper {

    public static void dropItems(World world, int x, int y, int z, ItemStack itemStack, Random rand) {

        if (itemStack != null) {
            float dX = rand.nextFloat() * 0.8F + 0.1F;
            float dY = rand.nextFloat() * 0.8F + 0.1F;
            float dZ = rand.nextFloat() * 0.8F + 0.1F;

            while (itemStack.stackSize > 0) {
                int j1 = rand.nextInt(21) + 10;

                if (j1 > itemStack.stackSize) {
                    j1 = itemStack.stackSize;
                }

                itemStack.stackSize -= j1;
                EntityItem entityitem = new EntityItem(world, (double) ((float) x + dX), (double) ((float) y + dY), (double) ((float) z + dZ), new ItemStack(itemStack.getItem(), j1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
                world.spawnEntityInWorld(entityitem);
            }
        }

    }

    //***Methods extract from vanilla hopper***//

    public static boolean inventoryIsEmpty(IInventory inventory, int side) {
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] slots = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int slot : slots) {
                if (isidedinventory.getStackInSlot(slot) != null) {
                    return false;
                }
            }
        } else {
            int j = inventory.getSizeInventory();

            for (int slot = 0; slot < j; ++slot) {
                if (inventory.getStackInSlot(slot) != null) {
                    return false;
                }
            }
        }

        return true;
    }


    public static boolean putEntityItemInInventory(IInventory inventory, EntityItem entityItem) {
        boolean flag = false;

        if (entityItem == null) {
            return false;
        } else {
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack itemstack1 = putItemInInventory(inventory, itemstack, 1);

            if (itemstack1 != null && itemstack1.stackSize != 0) {
                entityItem.setEntityItemStack(itemstack1);
            } else {
                flag = true;
                entityItem.setDead();
            }

            return flag;
        }
    }

    public static ItemStack putItemInInventory(IInventory inventory, ItemStack itemStack, int side) {
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] slots = isidedinventory.getAccessibleSlotsFromSide(side);

            for (int slot = 0; slot < slots.length && itemStack != null && itemStack.stackSize > 0; ++slot) {
                itemStack = putItemInSlot(inventory, itemStack, slots[slot], side);
            }
        } else {
            int j = inventory.getSizeInventory();

            for (int k = 0; k < j && itemStack != null && itemStack.stackSize > 0; ++k) {
                itemStack = putItemInSlot(inventory, itemStack, k, side);
            }
        }

        if (itemStack != null && itemStack.stackSize == 0) {
            itemStack = null;
        }

        return itemStack;
    }

    private static boolean isItemValidForSlotInInventory(IInventory inventory, ItemStack itemStack, int slot, int side) {
        return inventory.isItemValidForSlot(slot, itemStack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, side));
    }

    public static boolean canExtractItem(IInventory inventory, ItemStack itemStack, int slot, int side) {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(slot, itemStack, side);
    }

    private static ItemStack putItemInSlot(IInventory inventory, ItemStack itemStack, int slot, int side) {
        ItemStack itemStack1 = inventory.getStackInSlot(slot);

        if (isItemValidForSlotInInventory(inventory, itemStack, slot, side)) {
            boolean flag = false;

            if (itemStack1 == null) {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.stackSize) {
                    inventory.setInventorySlotContents(slot, itemStack);
                    itemStack = null;
                } else {
                    inventory.setInventorySlotContents(slot, itemStack.splitStack(max));
                }
                flag = true;
            } else if (itemStackAreEquals(itemStack1, itemStack)) {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max > itemStack1.stackSize) {
                    int l = Math.min(itemStack.stackSize, max - itemStack1.stackSize);
                    itemStack.stackSize -= l;
                    itemStack1.stackSize += l;
                    flag = l > 0;
                }
            }

            if (flag) {
                if (inventory instanceof CoolDownProvider) {
                    ((CoolDownProvider) inventory).setTransferCoolDown(8);
                    inventory.markDirty();
                }

                inventory.markDirty();
            }
        }

        return itemStack;
    }


    public static EntityItem getEntityItem(World world, double x, double y, double z) {
        List list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectAnything);
        return list.size() > 0 ? (EntityItem) list.get(0) : null;
    }

    public static IInventory getInventoryForCoordinates(World world, double x, double y, double z) {
        IInventory iinventory = null;
        int i = MathHelper.floor_double(x);
        int j = MathHelper.floor_double(y);
        int k = MathHelper.floor_double(z);
        TileEntity tileentity = world.getTileEntity(i, j, k);

        if (tileentity != null && tileentity instanceof IInventory) {
            iinventory = (IInventory) tileentity;

            if (iinventory instanceof TileEntityChest) {
                Block block = world.getBlock(i, j, k);

                if (block instanceof BlockChest) {
                    iinventory = ((BlockChest) block).func_149951_m(world, i, j, k);
                }
            }
        }

        if (iinventory == null) {
            List list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectInventories);

            if (list != null && list.size() > 0) {
                iinventory = (IInventory) list.get(world.rand.nextInt(list.size()));
            }
        }

        return iinventory;
    }

    private static boolean itemStackAreEquals(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1.getItem() == itemStack2.getItem() && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (itemStack1.stackSize <= itemStack1.getMaxStackSize() && ItemStack.areItemStackTagsEqual(itemStack1, itemStack2)));
    }

    public static boolean inventoryIsFull(IInventory inventory, int side) {
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory iSidedInventory = (ISidedInventory) inventory;
            int[] slots = iSidedInventory.getAccessibleSlotsFromSide(side);

            for (int slot : slots) {
                ItemStack itemStack = iSidedInventory.getStackInSlot(slot);

                if (itemStack == null || itemStack.stackSize != itemStack.getMaxStackSize()) {
                    return false;
                }
            }
        } else {
            int size = inventory.getSizeInventory();

            for (int slot = 0; slot < size; ++slot) {
                ItemStack itemstack = inventory.getStackInSlot(slot);

                if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

}


