package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.BlockFluidFurnace;
import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFluidFurnace extends TileEntityFluidHandler implements ITickable, ISidedInventory, IInteractionObject {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int[] SLOTS_TOP_SIDE = new int[]{INPUT};
    private static final int[] SLOTS_BOTTOM = new int[]{OUTPUT};
    private static final int LAVA_BURNING_TIME = getFluidBurningTime(new FluidStack(FluidRegistry.LAVA, 1));
    private static final int LAVA_TEMPERATURE = FluidRegistry.LAVA.getTemperature();
    protected final NonNullList<ItemStack> inventory; // input : 0 - output : 1
    private IItemHandler inputInventory;
    private IItemHandler outputInventory;

    private Fluid currentFluid = null;
    private boolean isCurrentFluidValid = false;
    private int fuelMaxLife = -1;
    private int tickToSmelt = -1;

    private int progress = 0;
    private int fuelLife = 0;

    private boolean isWorking = false;

    private UpgCFluidTankWrapper inputTank;
    private UpgCFluidTankWrapper outputTank;

    private String customName;

    public TileEntityFluidFurnace() {
        super(EnumCapacity.MACHINE_CAPACITY);
        this.inputTank = new UpgCFluidTankWrapper(this.fluidTank.getInternalTank(), false, true);
        this.outputTank = new UpgCFluidTankWrapper(this.fluidTank.getInternalTank(), true, false);
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.inputInventory = new SidedInvWrapper(this, EnumFacing.UP);
        this.outputInventory = new SidedInvWrapper(this, EnumFacing.DOWN);
    }

    private static int getFluidBurningTime(FluidStack fluidStack) {
        int burningTime = 0;

        if (fluidStack != null && fluidStack.getFluid() != null && fluidStack.amount > 0) {
            ItemStack bucket = new ItemStack(Items.BUCKET);
            IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(bucket);

            if (fluidHandler != null) {
                if (fluidHandler.fill(new FluidStack(fluidStack, Fluid.BUCKET_VOLUME), true) == Fluid.BUCKET_VOLUME) {
                    bucket = fluidHandler.getContainer();

                    burningTime = TileEntityFurnace.getItemBurnTime(bucket);
                }
            }
        }

        return burningTime;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        ItemStackHelper.loadAllItems(tag, this.inventory);

        this.progress = tag.getInteger("progress");
        this.fuelLife = tag.getInteger("fuelLife");

        if (tag.hasKey("isWorking"))
            this.isWorking = tag.getBoolean("isWorking");

        if (tag.hasKey("CustomName", 8)) {
            this.customName = tag.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);

        tag.setInteger("progress", this.progress);
        tag.setInteger("fuelLife", this.fuelLife);

        if (this.hasCustomName()) {
            tag.setString("CustomName", this.customName);
        }
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        super.writeToNBT(tag);
        tag.setBoolean("isWorking", this.isWorking);
        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        NBTTagCompound tag = new NBTTagCompound();
        super.writeToNBT(tag);
        tag.setBoolean("isWorking", this.isWorking);
        return new SPacketUpdateTileEntity(getPos(), 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        NBTTagCompound tag = packet.getNbtCompound();

        super.readFromNBT(tag);
        this.isWorking = tag.getBoolean("isWorking");
        this.updateLight();
    }

    @Override
    public void syncTileEntity() {
        markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (side != EnumFacing.DOWN)
            return SLOTS_TOP_SIDE;
        else return SLOTS_BOTTOM;
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nonnull EnumFacing direction) {
        return !(index >= 1 || direction == EnumFacing.DOWN) && this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return index == 1 && direction == EnumFacing.DOWN;
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
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq((double) this.getPos().getX() + 0.5D, (double) this.getPos().getY() + 0.5D, (double) this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {

        if (index >= 1)
            return false;
        else {
            ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(stack);
            return !smeltResult.isEmpty();

        }
    }

    private void invalidateFluid() {
        if (this.isCurrentFluidValid) {
            this.currentFluid = null;
            this.isCurrentFluidValid = false;
            this.fuelMaxLife = -1;
            this.fuelMaxLife = -1;
        }
    }

    private boolean canOperate() {


        FluidStack fluid;
        if ((fluid = getFluid()) != null && fluid.amount > 0) {
            if (fluid.getFluid() != this.currentFluid) {
                this.currentFluid = fluid.getFluid();
                this.isCurrentFluidValid = getFluidBurningTime(fluid) > 0;
            }
            if (this.isCurrentFluidValid) {

                ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.inventory.get(INPUT));

                if (!itemstack.isEmpty()) {
                    ItemStack outputItem = this.inventory.get(OUTPUT);
                    if (outputItem.isEmpty())
                        return true;
                    else if (outputItem.isItemEqual(itemstack)) {

                        int result = outputItem.getCount() + itemstack.getCount();
                        return result <= getInventoryStackLimit() && result <= outputItem.getMaxStackSize();

                    }
                }

            } else {
                this.fuelMaxLife = -1;
                this.fuelMaxLife = -1;
            }

        } else
            invalidateFluid();


        return false;

    }

    private void checkFluidSpeck() {

        if (this.fuelMaxLife < 0 || this.tickToSmelt < 0) {
            FluidStack fluidStack = getFluid();
            this.fuelMaxLife = (int) (((getFluidBurningTime(fluidStack)) / (float) (LAVA_BURNING_TIME)) * 20);
            this.tickToSmelt = (int) ((fluidStack.getFluid().getTemperature(fluidStack) / (float) LAVA_TEMPERATURE) * 200);

        }
    }

    private void operate() {
        if (++this.fuelLife >= this.fuelMaxLife) {
            this.fuelLife = 0;
            this.drain(1, true);
        }
        if (++this.progress >= this.tickToSmelt) {
            this.progress = 0;
            ItemStack input = this.inventory.get(INPUT);
            ItemStack output = this.inventory.get(OUTPUT);
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);

            if (output.isEmpty())
                this.inventory.set(OUTPUT, result.copy());
            else output.grow(result.getCount());

            if (input.getCount() == 1)
                this.inventory.set(INPUT, ItemStack.EMPTY);
            else input.shrink(1);

        }
    }

    @Override
    public void update() {

        if (!getWorld().isRemote) {
            if (canOperate()) {
                checkFluidSpeck();
                operate();

                if (!this.isWorking) {
                    this.isWorking = true;
                    changeState(this.isWorking());
                    syncTileEntity();
                }

            } else {
                if (this.progress != 0)
                    this.progress--;
                if (this.isWorking) {
                    this.isWorking = false;
                    changeState(this.isWorking());
                    syncTileEntity();
                }
            }

        }
    }


    private void changeState(boolean isWorking) {
        IBlockState state = getWorld().getBlockState(getPos());
        Block block = state.getBlock();
        if (block instanceof BlockFluidFurnace) {
            ((BlockFluidFurnace) block).changeWorkingStatus(getWorld(), getPos(), state, isWorking);
        }
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    @Override
    public int getField(int id) {

        switch (id) {
            case 0:
                return this.fuelMaxLife;
            case 1:
                return this.tickToSmelt;
            case 2:
                return this.progress;
            case 3:
                return this.fuelLife;
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.fuelMaxLife = value;
                break;
            case 1:
                this.tickToSmelt = value;
                break;
            case 2:
                this.progress = value;
                break;
            case 3:
                this.fuelLife = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
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

    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(ModBlocks.blockFluidFurnace.getUnlocalizedName() + ".name");
    }


    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFluidFurnace(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return ModBlocks.blockFluidFurnace.getUnlocalizedName();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale) {
        return (int) ((this.fuelLife * (float) scale) / this.fuelMaxLife);

    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale) {
        return (int) ((this.progress * (float) scale) / this.tickToSmelt);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing != null) {
                if (facing == EnumFacing.DOWN)
                    return (T) this.outputInventory;
                else return (T) this.inputInventory;
            } else return null;
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            if (facing != null) {
                if (facing == EnumFacing.DOWN)
                    return (T) this.outputTank;
                else return (T) this.inputTank;
            } else return null;

        return super.getCapability(capability, facing);
    }


}
