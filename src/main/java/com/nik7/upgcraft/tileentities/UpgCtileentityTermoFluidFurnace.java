package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluid.ActiveLava;
import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.registry.TermoSmelting.TermoSmeltingRecipe;
import com.nik7.upgcraft.registry.TermoSmeltingRegister;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import com.nik7.upgcraft.util.physicsHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class UpgCtileentityTermoFluidFurnace extends UpgCtileentityInventoryFluidHandler {

    //tank[0]: fuel - tank[1]: working tank - tank[2]: waste
    //itemStacks[0]: input - itemStacks[1]: output - itemStacks[2 - 3]: waste - itemStack[4]: waste fluid output

    public static final int MAX_TEMPERATURE = 5700;

    private static final int DIAMOND_THERMAL_CONDUCTIVITY = 1600;
    public float internalTemp = 273;
    private float workingTemp = 0;
    private int workingTick = 0;
    private int activeCycles = 1;
    private static final float INTERNAL_MASS = 1 * 0.004f; // volume * density
    public static final int INTERNAL_CAPACITY_WORKING_TANK = 10 * FluidContainerRegistry.BUCKET_VOLUME;
    private boolean canOperate = true;

    private boolean balanced = false;
    private int balancedTicks = 0;

    public static final int MIN_OPERATION_TEMP = 270 + 273;
    private static final int STD_TEMP = 300 + 273;
    private static final int STD_BURN_TIME = 200;
    public int smeltingTicks = 0;
    public int totalSmeltingTicks;
    private boolean canSmelt = false;
    private boolean isTermoSmelting;
    private TermoSmeltingRecipe termoSmeltingRecipe;

    public int wasteFluidAmount;

    public boolean isActive = false;

    public UpgCtileentityTermoFluidFurnace() {
        this.tank = new UpgCActiveTank[]{new UpgCActiveTank(Capacity.INTERNAL_FLUID_TANK_TR1, this), new UpgCActiveTank(INTERNAL_CAPACITY_WORKING_TANK, this), new UpgCActiveTank(INTERNAL_CAPACITY_WORKING_TANK, this)};
        this.itemStacks = new ItemStack[5];
        this.capacity = Capacity.INTERNAL_FLUID_TANK_TR1;
    }


    @Override
    public void writeToPacket(ByteBuf buf) {
        buf.writeBoolean(isActive);
        writeFluidToByteBuf(this.tank[0], buf);
    }

    @Override
    public void readFromPacket(ByteBuf buf) {
        this.isActive = buf.readBoolean();
        readFluidToByteBuf(this.tank[0], buf);
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.internalTemp = tag.getFloat("internalTemp");
        this.workingTemp = tag.getFloat("workingTemp");
        this.workingTick = tag.getInteger("workingTick");
        this.canOperate = tag.getBoolean("canOperate");
        this.activeCycles = tag.getInteger("activeCycles");

        this.canSmelt = tag.getBoolean("canSmelt");
        this.smeltingTicks = tag.getInteger("smeltingTicks");
        this.totalSmeltingTicks = tag.getInteger("totalSmeltingTicks");
        this.isTermoSmelting = tag.getBoolean("isTermoSmelting");

        this.wasteFluidAmount = tag.getInteger("wasteFluidAmount");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setFloat("internalTemp", this.internalTemp);
        tag.setFloat("workingTemp", this.workingTemp);
        tag.setInteger("workingTick", this.workingTick);
        tag.setBoolean("canOperate", this.canOperate);
        tag.setInteger("activeCycles", this.activeCycles);

        tag.setBoolean("canSmelt", this.canSmelt);
        tag.setInteger("smeltingTicks", this.smeltingTicks);
        tag.setInteger("totalSmeltingTicks", this.totalSmeltingTicks);
        tag.setBoolean("isTermoSmelting", this.isTermoSmelting);

        tag.setInteger("wasteFluidAmount", this.wasteFluidAmount);

    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tank[0].getFluid() == null ? 0 : tank[0].getFluid().amount) / capacity;
    }

    @SideOnly(Side.CLIENT)
    public float getWasteFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (wasteFluidAmount) / INTERNAL_CAPACITY_WORKING_TANK;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {

        if (totalSmeltingTicks <= 0)
            return 0;

        return (int) ((float) this.smeltingTicks * scaleFactor) / this.totalSmeltingTicks;
    }

    @SideOnly(Side.CLIENT)
    public int geTemperatureScaled(int scaleFactor) {

        if (internalTemp <= 273)
            return 0;

        int result = (int) (internalTemp - 273) * scaleFactor / (MAX_TEMPERATURE - 273);

        if (result == 0 && internalTemp > 273 + 100)
            return 1;

        return result;
    }

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {

            if (canOperate) {

                if ((workingTick % 20) == 0) {
                    getWorkingFuel();
                }
                if ((workingTick % 20) == 1 && internalTemp < MAX_TEMPERATURE) {
                    if (workingTemp == 0 && tank[1].getFluid() != null)
                        if (!balanced)
                            TempOperation();
                        else {
                            if (++balancedTicks > 6) {
                                balanced = false;
                                balancedTicks = 0;
                            }
                        }
                }

                if ((workingTick % 80) == 10) {
                    if (Math.random() > internalTemp / (double) MAX_TEMPERATURE) {
                        if (!balanced)
                            wasteOperation();
                        else if (Math.random() < 0.22) {
                            wasteOperation();
                        }
                    }
                }
            }

            if (!canOperate && (workingTick % 20) == 3)
                checkOperation();

            canSmelt();

            if (canSmelt) {
                smelting();

            } else {
                smeltingTicks = 0;
                isActive = false;
            }

            if ((workingTick % 20) == 19) {
                if (internalTemp > 273)
                    internalTemp--;
                else if (internalTemp < 273)
                    internalTemp++;
            }

            if (workingTemp != 0 && internalTemp < MAX_TEMPERATURE) {
                if (workingTemp > 0) {
                    internalTemp++;
                    workingTemp--;
                } else {
                    internalTemp--;
                    workingTemp++;
                    workingTemp = Math.round(workingTemp);
                }
            }

            if ((workingTick % 20) == 10) {
                outPutWasteFluid();
            }

            workingTick++;
            workingTick %= 20000;


        }
    }

    private void checkOperation() {

        canOperate = !((itemStacks[2] != null && itemStacks[2].stackSize == 64) || (itemStacks[3] != null && itemStacks[3].stackSize == 64) || tank[2].getCapacity() == tank[2].getFluidAmount());
    }

    private void outPutWasteFluid() {
        FluidStack fluid = tank[2].getFluid();
        if (fluid != null) {
            if (itemStacks[4] != null && itemStacks[4].stackSize == 1) {
                ItemStack itemStack = itemStacks[4];
                if (FluidContainerRegistry.isEmptyContainer(itemStack)) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                        FluidStack oneBucketOfFluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);
                        itemStacks[4] = filledBucket;
                        fluid.amount -= FluidContainerRegistry.BUCKET_VOLUME;
                    }
                } else if (itemStack.getItem() instanceof IFluidContainerItem) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {

                        IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();
                        int amount = fluidContainerItem.fill(itemStack, fluid, true);
                        if (amount > 0) {
                            fluid.amount -= amount;
                        }
                    }

                }
            }
        }
    }

    private void smelting() {

        this.totalSmeltingTicks = smeltingTime();

        if (totalSmeltingTicks != -1) {

            if (smeltingTicks >= totalSmeltingTicks) {
                smeltItem();
                smeltingTicks = 0;

                if (isTermoSmelting)
                    internalTemp -= 100;
                else internalTemp -= 25;

            } else {
                if (isTermoSmelting)
                    internalTemp -= (int) (2f * Math.random());
                isActive = true;
                smeltingTicks++;
            }


        }

    }

    private void smeltItem() {

        ItemStack itemstack;
        if (isTermoSmelting) {
            if (termoSmeltingRecipe == null) {
                isTermoSmelting = false;
                canSmelt = false;
                return;
            }
            itemstack = termoSmeltingRecipe.getOutput();
        } else {
            itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);
        }

        if (itemStacks == null) {
            isTermoSmelting = false;
            canSmelt = false;
            return;
        }

        if (this.itemStacks[1] == null) {
            this.itemStacks[1] = itemstack.copy();
        } else if (this.itemStacks[1].getItem() == itemstack.getItem()) {
            this.itemStacks[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
        }

        --this.itemStacks[0].stackSize;

        if (this.itemStacks[0].stackSize <= 0) {
            this.itemStacks[0] = null;
        }

    }

    private int smeltingTime() {
        if (!isTermoSmelting) {
            if (internalTemp == 273)
                return -1;

            return (int) ((STD_TEMP / internalTemp) * STD_BURN_TIME);
        } else {
            if (termoSmeltingRecipe == null) {
                isTermoSmelting = false;
                canSmelt = false;
                return -1;
            }
            if (internalTemp < termoSmeltingRecipe.getTemperature())
                return -1;
            return (int) ((termoSmeltingRecipe.getTemperature() / internalTemp) * termoSmeltingRecipe.getTicks());
        }
    }

    private void canNormaSmelt() {
        if (this.itemStacks[0] != null && internalTemp >= MIN_OPERATION_TEMP) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);

            if (itemstack == null) {
                canSmelt = false;
                return;
            }
            if (this.itemStacks[1] == null) {
                canSmelt = true;
                return;
            }
            if (!this.itemStacks[1].isItemEqual(itemstack)) {
                canSmelt = false;
                return;
            }
            int result = itemStacks[1].stackSize + itemstack.stackSize;
            canSmelt = result <= getInventoryStackLimit() && result <= this.itemStacks[1].getMaxStackSize();
        } else
            canSmelt = false;
    }

    private void canTermoSmelt() {
        isTermoSmelting = false;
        if (this.itemStacks[0] != null) {

            termoSmeltingRecipe = TermoSmeltingRegister.getRecipeFromInput(this.itemStacks[0]);

            if (termoSmeltingRecipe != null && termoSmeltingRecipe.getTemperature() <= internalTemp) {
                isTermoSmelting = true;
                if (this.itemStacks[1] == null) {
                    canSmelt = true;
                    return;
                }
                if (!this.itemStacks[1].isItemEqual(termoSmeltingRecipe.getOutput())) {
                    canSmelt = false;
                    return;
                }
                int result = itemStacks[1].stackSize + termoSmeltingRecipe.getOutput().stackSize;
                canSmelt = result <= getInventoryStackLimit() && result <= this.itemStacks[1].getMaxStackSize();
                return;
            }

        }

        canSmelt = false;
        isTermoSmelting = false;

    }

    private void canSmelt() {
        canSmelt = false;
        if (!isTermoSmelting)
            canNormaSmelt();
        if (!canSmelt) {
            canTermoSmelt();
        }

    }

    private void getWorkingFuel() {

        if (tank[0].getFluid() == null) {
            return;
        }

        if (tank[0].getFluid().amount <= 0) {
            tank[0].setFluid(null);
            updateModBlock();
            return;
        }

        int amountToAsk = tank[1].fill(new FluidStack(tank[0].getFluid().getFluid(), tank[1].getCapacity()), false);
        FluidStack fluidStack = tank[0].drain(amountToAsk, true);
        tank[1].fill(fluidStack, true);
        updateModBlock();
    }


    private void TempOperation() {
        this.workingTemp = physicsHelper.getFinalTemp(tank[1].getFluid(), internalTemp, INTERNAL_MASS, 20, heatLost());

        if (((int) Math.abs(workingTemp)) <= 12)
            balanced = true;

    }

    private void wasteOperation() {
        wasteFluidAmount = 0;
        if (tank[1].getFluid() != null) {
            if (!(this.tank[1].getFluid().getFluid() == ModFluids.ActiveLava)) {
                int amountToDrain = (int) ((FluidContainerRegistry.BUCKET_VOLUME / 4f) - FluidContainerRegistry.BUCKET_VOLUME / 3.8f * (internalTemp / this.tank[1].getFluid().getFluid().getTemperature(this.tank[1].getFluid())));
                if (amountToDrain > 0) {
                    FluidStack fluidStack = this.tank[1].drain(amountToDrain, true);
                    if (fluidStack.getFluid().getTemperature(fluidStack) < 100 + 273 && (fluidStack.getFluid().getBlock() != null && fluidStack.getFluid().getBlock().getMaterial() == Material.water)) {
                        int fluidTemp = fluidStack.getFluid().getTemperature(fluidStack);
                        int dTemp = (int) ((internalTemp + workingTemp) - fluidTemp);
                        if (dTemp > 120)
                            tank[1].setFluid(null);
                    } else {
                        double rnd = Math.random();
                        if (fluidStack.amount == amountToDrain) {
                            if (rnd <= 0.15) {
                                addCobblestone();

                            } else {

                                if (rnd <= 0.28) {
                                    addObsidian();
                                } else if (rnd <= 0.78) {
                                    addCobblestone();
                                }

                            }

                        } else {
                            if (rnd <= 0.36) {
                                addCobblestone();

                            }

                        }

                    }
                }
            } else {
                FluidStack ActiveFluidStack = tank[1].getFluid();
                int temp = ActiveFluidStack.getFluid().getTemperature(ActiveFluidStack);
                if (temp < this.internalTemp - 200) {
                    int principalTemp = tank[0].getFluid() == null ? 0 : tank[0].getFluid().getFluid().getTemperature(tank[0].getFluid());
                    if (principalTemp > temp) {
                        int amountToDrain = (1 + (int) (8 * internalTemp / temp)) * FluidContainerRegistry.BUCKET_VOLUME;
                        FluidStack fluidStack = tank[1].drain(amountToDrain, true);
                        if (tank[2].fill(fluidStack, true) != fluidStack.amount)
                            canOperate = false;
                    }
                }

                if (tank[1].getFluid() != null) {
                    wasteFluidAmount = tank[1].getFluid().amount;
                } else wasteFluidAmount = 0;

                if ((activeCycles % 20) == 3)
                    ((ActiveLava) tank[1].getFluid().getFluid()).decreaseActiveValue(tank[1].getFluid());

                activeCycles++;
                activeCycles %= 200;

                double rnd = Math.random();
                if ((activeCycles % 4) == 0) {
                    if (rnd <= 0.32) {
                        addObsidian();
                    } else if (rnd <= 0.81) {
                        addCobblestone();
                    }

                } else {
                    if (rnd <= 0.12) {
                        addObsidian();
                    } else if (rnd <= 0.46) {
                        addCobblestone();
                    }
                }
            }
        }
        updateModBlock();
    }

    private void addCobblestone() {
        if (this.itemStacks[2] == null) {
            this.itemStacks[2] = new ItemStack(Blocks.cobblestone);
        } else if (this.itemStacks[2].stackSize < this.getSizeInventory() && this.itemStacks[2].stackSize < this.itemStacks[2].getItem().getItemStackLimit(itemStacks[2])) {
            this.itemStacks[2].stackSize++;
        } else {
            canOperate = false;
        }
    }

    private void addObsidian() {
        if (this.itemStacks[3] == null) {
            this.itemStacks[3] = new ItemStack(Blocks.obsidian);
        } else if (this.itemStacks[3].stackSize < this.getSizeInventory() && this.itemStacks[3].stackSize < this.itemStacks[3].getItem().getItemStackLimit(itemStacks[3])) {
            this.itemStacks[3].stackSize++;
        } else {
            canOperate = false;
        }
    }


    private float heatLost() {
        int t = tank[0].getFluid() == null ? 0 : tank[0].getFluid().getFluid().getTemperature(tank[0].getFluid());
        return (float) (Math.abs((t - (double) internalTemp)) / DIAMOND_THERMAL_CONDUCTIVITY);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return super.fill(from, resource, 0, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return super.drain(from, resource, 1, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return super.drain(from, maxDrain, 1, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return super.getTankInfo(from, 0);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot == 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int size) {
        return slot == 1 || slot == 2 || slot == 3;
    }

    @Override
    public String getInventoryName() {
        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_TERMO_FURNACE;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot == 0;
    }
}
