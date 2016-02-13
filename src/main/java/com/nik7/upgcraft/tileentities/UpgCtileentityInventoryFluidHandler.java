package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluids.IMultipleTankFluidHandler;
import com.nik7.upgcraft.network.DescriptionHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class UpgCtileentityInventoryFluidHandler extends TileEntity implements ISidedInventory, IMultipleTankFluidHandler {

    private final int inventorySize;
    private final int tanksNumber;
    protected ItemStack[] inventory;
    protected FluidTank[] tanks;

    protected String customName = null;
    private final String name;

    @SideOnly(Side.CLIENT)
    private int meta = 0;

    protected UpgCtileentityInventoryFluidHandler(ItemStack[] inventory, FluidTank[] tanks, String name) {

        this.inventorySize = inventory.length;
        this.tanksNumber = tanks.length;
        this.name = name;

        this.inventory = inventory;
        this.tanks = tanks;

    }

    @SideOnly(Side.CLIENT)
    public void setBlockType(Block blockType) {
        if (this.blockType == null)
            this.blockType = blockType;
    }

    @SideOnly(Side.CLIENT)
    public void setMetadata(int metadata) {
        this.meta = metadata;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockMetadataClient() {
        return this.meta;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        //tank
        NBTTagList nbtTagList = tag.getTagList("Tanks", 10);
        for (int i = 0; i < nbtTagList.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");
            if (b0 >= 0 && b0 < this.tanks.length) {
                this.tanks[b0].readFromNBT(nbtTagCompound);
            }

        }

        //itemStack
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
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);


        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.tanks.length; ++i) {

            if (this.tanks[i] != null) {

                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.tanks[i].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Tanks", nbtTagList);

        //itemStack
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

        if (this.hasCustomName()) {
            tag.setString("CustomName", this.customName);
        }

    }

    @Override
    public Packet getDescriptionPacket() {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        writeToPacket(buf);
        return new FMLProxyPacket(buf, DescriptionHandler.CHANNEL);
    }

    protected void writeFluidToByteBuf(FluidTank tank, PacketBuffer buf) {

        FluidStack fluidStack = tank != null ? tank.getFluid() : null;
        int fluidAmount = -1;
        String fluidID = "";
        int extraValue = -1;
        if (fluidStack != null) {
            fluidAmount = fluidStack.amount;
            fluidID = fluidStack.getFluid().getName();
            /*if (fluidStack.getFluid() == ModFluids.ActiveLava)
                extraValue = ((ActiveLava) fluidStack.getFluid()).getActiveValue(fluidStack);*/
        }

        buf.writeInt(fluidAmount);
        buf.writeInt(fluidID.length());
        buf.writeString(fluidID);
        buf.writeInt(extraValue);
    }

    protected void readFluidToByteBuf(FluidTank tank, PacketBuffer buf) {

        int fluidAmount = buf.readInt();
        int stringL = buf.readInt();
        String fluidID = buf.readStringFromBuffer(stringL);
        int extraValue = buf.readInt();
        if (fluidAmount > 0) {
            FluidStack fluidStack = new FluidStack(FluidRegistry.getFluid(fluidID), fluidAmount);
            /*if (fluidStack.getFluid() == ModFluids.ActiveLava) {
                ((ActiveLava) fluidStack.getFluid()).setActiveValue(fluidStack, extraValue);
            }*/
            tank.setFluid(fluidStack);
        } else tank.setFluid(null);

        worldObj.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

    }


    public void writeToPacket(PacketBuffer buf) {

    }

    public void readFromPacket(PacketBuffer buf) {

    }


    protected void updateModBlock() {
        //worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
        worldObj.markBlockForUpdate(pos);
        //this.worldObj.notifyBlockOfStateChange(pos, getBlockType());
        this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
    }


    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {

        if (this.inventory[index] != null) {
            if (this.inventory[index].stackSize <= count) {
                ItemStack itemStack = this.inventory[index];
                this.inventory[index] = null;
                return itemStack;
            } else {
                ItemStack itemstack = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0) {
                    this.inventory[index] = null;
                }

                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        if (inventory[index] != null) {
            ItemStack itemStack = inventory[index];
            inventory[index] = null;
            return itemStack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

    }


    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;

    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }


    @Override
    public void clear() {

        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }

    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : this.name;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    //fluid part

    @Override
    public int getTanksNumber() {
        return tanksNumber;
    }

    @Override
    public int fill(int tank, EnumFacing from, FluidStack resource, boolean doFill) {

        if (tank < getTanksNumber() && resource != null && canFill(tank, from, resource.getFluid())) {
            int result = tanks[tank].fill(resource, doFill);
            if (result != 0)
                updateModBlock();
            return result;
        }

        return 0;
    }

    @Override
    public FluidStack drain(int tank, EnumFacing from, FluidStack resource, boolean doDrain) {

        if (resource != null && tank < getTanksNumber() && canDrain(tank, from, resource.getFluid())) {
            if (resource.getFluid() != null && resource.amount > 0) {
                FluidStack intTank = tanks[tank].getFluid();
                if (resource.isFluidEqual(intTank)) {
                    return this.drain(tank, from, resource.amount, doDrain);
                }

            }
        }
        return null;
    }

    @Override
    public FluidStack drain(int tank, EnumFacing from, int maxDrain, boolean doDrain) {

        if (tank < getTanksNumber()) {
            FluidStack result = tanks[tank].drain(maxDrain, doDrain);
            if (result != null)
                updateModBlock();
            return result;
        }

        return null;
    }

    @Override
    public boolean canFill(int tank, EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(int tank, EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(int tank, EnumFacing from) {
        return new FluidTankInfo[]{tanks[tank].getInfo()};
    }

    public FluidStack getFluid(int tank) {
        if (tanks[tank].getFluid() != null)
            return new FluidStack(tanks[tank].getFluid(), tanks[tank].getFluidAmount());
        else return null;
    }

    public abstract int getTankToShow();

    public float getFillPercentage(int tank) {
        int fluidAmount = tanks[tank].getFluidAmount();
        if (fluidAmount > 0) {
            return (float) fluidAmount / (float) tanks[tank].getCapacity();
        }
        return 0;

    }


}
