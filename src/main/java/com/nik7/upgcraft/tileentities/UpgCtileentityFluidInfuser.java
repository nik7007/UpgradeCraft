package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class UpgCtileentityFluidInfuser extends UpgCtileentityInventoryFluidHandler {

    public static int MELT = 0;
    public static int INFUSE = 1;
    private static final int[] input = {MELT, INFUSE};
    public static int OUTPUT = 4;
    private static final int[] output = {OUTPUT};
    public static int INFUSE_P = 3;
    public static short FLUID_AMOUNT = 0, FLUID_ID = 1, TICK_FOR_MELT = 2, NUMBER_TO_MELT = 3, TICK_FOR_INFUSE = 4, NUMBER_TO_INFUSE = 5;
    private static int MELT_P = 2;
    public boolean isActive = false;
    public int[] properties = {0, -1, 0, 0, 0, 0};
    public int tickMelting = 0;
    public int tickInfusing = 0;
    private boolean isOperating = false;
    private int burning = 0;


    public UpgCtileentityFluidInfuser() {
        this.tank = new UpgCTank(Capacity.INTERNAL_FLUID_TANK_TR1);
        this.itemStacks = new ItemStack[5];
        this.capacity = Capacity.INTERNAL_FLUID_TANK_TR1;

    }

    @Override
    public void writeToPacket(ByteBuf buf) {
        FluidStack fluidStack = this.tank.getFluid();
        int fluidAmount = -1;
        int fluidID = -1;
        if (fluidStack != null) {
            fluidAmount = fluidStack.amount;
            fluidID = fluidStack.getFluidID();
        }

        buf.writeInt(fluidAmount);
        buf.writeInt(fluidID);
        buf.writeBoolean(isActive);

    }

    @Override
    public void readFromPacket(ByteBuf buf) {

        int fluidAmount = buf.readInt();
        int fluidID = buf.readInt();
        this.isActive = buf.readBoolean();

        if (fluidAmount > 0) {

            this.tank.setFluid(new FluidStack(FluidRegistry.getFluid(fluidID), fluidAmount));
            this.fluidLevel = fluidAmount;
        }

        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.isOperating = tag.getBoolean("isOperating");
        this.properties = tag.getIntArray("properties");
        this.tickMelting = tag.getInteger("tickMelting");
        this.tickInfusing = tag.getInteger("tickInfusing");
        this.burning = tag.getInteger("burning");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isOperating", this.isOperating);
        tag.setIntArray("properties", this.properties);
        tag.setInteger("tickMelting", this.tickMelting);
        tag.setInteger("tickInfusing", this.tickInfusing);
        tag.setInteger("burning", this.burning);

    }

    @Override
    public void updateEntity() {

        if (!worldObj.isRemote) {
            boolean oldIsActive = isActive;

            if (this.tank.getFluidAmount() > 0) {

                if (isOperating) {
                    if (canContinue()) {
                        operating();
                    } else {
                        burning = 0;
                    }

                } else {
                    isOperating = canStart();
                }
                isActive = isOperating && (tickInfusing > 0 || tickMelting > 0);
            }

            if (isActive != oldIsActive) {
                markDirty();
                updateModBlock();
            }
        }

    }

    private void operating() {
        if (burning == 0 && tickMelting < this.properties[TICK_FOR_MELT]) {
            FluidStack fluidBurned = this.tank.drain((this.properties[FLUID_AMOUNT] / this.properties[TICK_FOR_MELT]), true);
            burning = fluidBurned.amount;
        }

        if (tickMelting < this.properties[TICK_FOR_MELT]) {
            melting();
        } else if (tickInfusing < this.properties[TICK_FOR_INFUSE]) {
            infusing();
        } else if (tickMelting == properties[TICK_FOR_MELT] && tickInfusing == properties[TICK_FOR_INFUSE]) {
            infuse();
        } else {
            LogHelper.error("This case should not be possible!!!");
        }


    }

    private void infuse() {

        FluidInfuserRecipe recipe = FluidInfuserRegister.getFluidInfuserRecipe(itemStacks[MELT_P], itemStacks[INFUSE_P], getFluid());

        if(recipe==null)
        {
            LogHelper.fatal("Fluid Infuser recipe is nul!!");
            return;
        }

        itemStacks[MELT_P] = null;
        itemStacks[INFUSE_P] = null;

        if (itemStacks[OUTPUT] == null) {

            itemStacks[OUTPUT] = new ItemStack(recipe.getResult().getItem(), recipe.getResult().stackSize, recipe.getResult().getItemDamage());

        } else {
            itemStacks[OUTPUT].stackSize += recipe.getResult().stackSize;
        }
        cleanProcess();
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) fluidLevel / capacity;
    }

    @SideOnly(Side.CLIENT)
    public int getInfusingTimeRemainingScaled(int scaleFactor) {

        if (this.properties[TICK_FOR_INFUSE] != 0 && tickInfusing != 0) {
            return tickInfusing * scaleFactor / this.properties[TICK_FOR_INFUSE];
        } else return 0;

    }

    private void infusing() {
        tickInfusing++;
        if (itemStacks[INFUSE] != null && itemStacks[INFUSE_P] == null) {
            itemStacks[INFUSE_P] = new ItemStack(itemStacks[INFUSE].getItem(), properties[NUMBER_TO_INFUSE], itemStacks[INFUSE].getItemDamage());
            itemStacks[INFUSE].stackSize -= properties[NUMBER_TO_INFUSE];
            if (itemStacks[INFUSE].stackSize <= 0) {
                itemStacks[INFUSE] = null;
            }
            this.properties[NUMBER_TO_INFUSE] = 0;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getMeltingTimeRemainingScaled(int scaleFactor) {

        if (this.properties[TICK_FOR_MELT] != 0 && tickMelting != 0) {
            return tickMelting * scaleFactor / this.properties[TICK_FOR_MELT];
        } else return 0;

    }

    private void melting() {
        if ((burning > 0)) {
            burning = 0;
            tickMelting++;

            if ((tickMelting % (this.properties[TICK_FOR_MELT] / this.properties[NUMBER_TO_MELT])) == 0) {
                this.moveItem(MELT, MELT_P);
                this.properties[NUMBER_TO_MELT]--;
                burning = 0;
            }

        }
    }

    private void moveItem(int from, int to) {
        if (itemStacks[from] == null) {
            return;
        }
        if (itemStacks[from].stackSize <= 0) {
            itemStacks[from] = null;
            return;
        }

        if (itemStacks[to] == null) {
            itemStacks[to] = new ItemStack(itemStacks[from].getItem(), 1);
        } else {
            itemStacks[to].stackSize++;
        }

        itemStacks[from].stackSize--;

        if (itemStacks[from].stackSize <= 0) {
            itemStacks[from] = null;

        }


    }

    private boolean canStart() {

        if (itemStacks[MELT] == null || itemStacks[INFUSE] == null) {
            return false;
        }

        FluidInfuserRecipe recipe = FluidInfuserRegister.getFluidInfuserRecipe(itemStacks[MELT], itemStacks[INFUSE], getFluid());

        if (recipe != null) {

            if ((itemStacks[OUTPUT] != null) && (itemStacks[OUTPUT].getItem() != recipe.getResult().getItem()) && (itemStacks[OUTPUT].getItemDamage() == recipe.getResult().getItemDamage())) {
                return false;
            } else if (itemStacks[OUTPUT] != null && itemStacks[OUTPUT].stackSize >= 64) {
                return false;
            }

            FluidStack otherFluid = recipe.getFluidStack();

            int nToMelt = recipe.getNumberItemToMelt();
            int nToInfuse = recipe.getNumberItemToInfuse();

            boolean result = itemStacks[MELT].stackSize >= recipe.getNumberItemToMelt() && itemStacks[INFUSE].stackSize >= recipe.getNumberItemToInfuse(); // for prevent any possibles future bugs...

            if (!result) {
                return false;
            }

            FluidStack fluid = this.tank.getFluid();

            if (fluid != null) {
                result = fluid.containsFluid(otherFluid) && fluid.amount >= (otherFluid.amount / recipe.getTicksToMelt());
            } else {
                return false;
            }

            if (result) {
                this.initProcess(otherFluid.amount, otherFluid.getFluidID(), recipe.getTicksToMelt(), nToMelt, recipe.getTicksToInfuse(), nToInfuse);
            }

            return result;
        }


        return false;
    }

    private boolean canContinue() {
        ItemStack melt = itemStacks[MELT];
        ItemStack infuse = itemStacks[INFUSE];

        int nMelt = this.properties[NUMBER_TO_MELT];
        int nInfuse = this.properties[NUMBER_TO_INFUSE];
        int nFluid = this.properties[FLUID_AMOUNT];
        int fluidAmount = this.tank.getFluidAmount();

        return !((nMelt > 0 && melt == null) || (nInfuse > 0 && infuse == null) || fluidAmount < nFluid);

    }

    private void initProcess(int fluidAmount, int fluidID, int tToMelt, int nToMelt, int tToInfuse, int nToInfuse) {
        this.properties[FLUID_AMOUNT] = fluidAmount;
        this.properties[FLUID_ID] = fluidID;
        this.properties[TICK_FOR_MELT] = tToMelt;
        this.properties[TICK_FOR_INFUSE] = tToInfuse;

        this.properties[NUMBER_TO_MELT] = nToMelt;
        this.properties[NUMBER_TO_INFUSE] = nToInfuse;


    }

    private void cleanProcess() {
        this.properties = new int[]{0, -1, 0, 0, 0, 0};
        this.tickMelting = 0;
        this.tickInfusing = 0;
        this.burning = 0;
        this.isOperating = false;

    }


    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {

        if (fluid != null && FluidInfuserRegister.isUsefulFluid(fluid)) {

            if ((from != ForgeDirection.UP || from != ForgeDirection.DOWN)) {
                return this.properties[FLUID_ID] == -1 || this.properties[FLUID_ID] == fluid.getID();
            }
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.DOWN;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0)
            return output;
        else
            return input;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return (slot == MELT || slot == INFUSE) && side != 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int size) {

        if (slot == MELT || slot == INFUSE) {
            return false;
        } else if (slot == OUTPUT && size == 0) {
            return true;
        }

        return false;
    }

    @Override
    public String getInventoryName() {

        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_FLUID_INFUSER;
    }


    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {


        if (itemStack == null) return true;
        else if (slot == MELT) {

            if (itemStacks[MELT] != null && itemStacks[MELT].getItem() == itemStack.getItem()) {
                return true;
            } else if (itemStacks[MELT] != null && itemStacks[MELT].getItem() != itemStack.getItem()) return false;

            else if (itemStacks[MELT] == null) {
                boolean result = FluidInfuserRegister.canBeMelted(itemStack);
                if (result && itemStacks[INFUSE] != null) {
                    result = FluidInfuserRegister.canBeMelted(itemStack, itemStacks[INFUSE]);
                }
                return result;
            }
        } else if (slot == INFUSE) {

            if (itemStacks[INFUSE] != null && itemStacks[INFUSE].getItem() == itemStack.getItem()) {
                return true;
            } else if (itemStacks[INFUSE] != null && itemStacks[INFUSE].getItem() != itemStack.getItem()) return false;
            else if (itemStacks[INFUSE] == null) {
                boolean result = FluidInfuserRegister.canBeInfused(itemStack);
                if (result && itemStacks[MELT] != null) {
                    result = FluidInfuserRegister.canBeInfused(itemStack, itemStacks[MELT]);
                }
                return result;
            }
        }

        return false;
    }

}
