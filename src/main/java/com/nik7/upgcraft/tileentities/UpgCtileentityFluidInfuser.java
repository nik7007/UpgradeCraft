package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpgCtileentityFluidInfuser extends UpgCtileentityInventoryFluidHandler implements IInteractionObject {

    public static final int MELT = 0;
    public static final int INFUSE = 1;
    private static final int[] input = {MELT, INFUSE};
    public static final int OUTPUT = 4;
    private static final int[] output = {OUTPUT};
    public static final int INFUSE_P = 3;
    public static final short FLUID_AMOUNT = 0, FLUID_ID = 1, TICK_FOR_MELT = 2, NUMBER_TO_MELT = 3, TICK_FOR_INFUSE = 4, NUMBER_TO_INFUSE = 5;
    public static final int MELT_P = 2;
    private boolean isActive = false;
    private Object[] properties = {0, "", 0, 0, 0, 0};
    private int tickMelting = 0;
    private int tickInfusing = 0;
    private boolean isOperating = false;
    private int burning = 0;


    public UpgCtileentityFluidInfuser() {
        super(new ItemStack[5], new UpgCFluidTank[]{new UpgCFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1)}, "FluidInfuser");
        this.tanks[0].setTileEntity(this);
    }

    public boolean isFluidHot() {
        return tanks[0].isFluidHot();
    }

    @Override
    public void writeToPacket(PacketBuffer buf) {
        buf.writeBoolean(isActive);
        writeFluidToByteBuf(this.tanks[0], buf);
    }

    @Override
    public void readFromPacket(PacketBuffer buf) {
        super.readFromPacket(buf);
        this.isActive = buf.readBoolean();
        readFluidToByteBuf(this.tanks[0], buf);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.isOperating = tag.getBoolean("isOperating");

        readPropertiesFromNBT(tag);

        this.tickMelting = tag.getInteger("tickMelting");
        this.tickInfusing = tag.getInteger("tickInfusing");
        this.burning = tag.getInteger("burning");

    }

    private void readPropertiesFromNBT(NBTTagCompound tag) {
        this.properties[FLUID_AMOUNT] = tag.getInteger("fluidAmount");
        this.properties[FLUID_ID] = tag.getString("fluidID");
        this.properties[TICK_FOR_MELT] = tag.getInteger("tickForMelt");
        this.properties[NUMBER_TO_MELT] = tag.getInteger("numberToMelt");
        this.properties[TICK_FOR_INFUSE] = tag.getInteger("tickForInfuse");
        this.properties[NUMBER_TO_INFUSE] = tag.getInteger("numberToInfuse");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isOperating", this.isOperating);
        writePropertiesFromNBT(tag);
        tag.setInteger("tickMelting", this.tickMelting);
        tag.setInteger("tickInfusing", this.tickInfusing);
        tag.setInteger("burning", this.burning);

    }

    private void writePropertiesFromNBT(NBTTagCompound tag) {
        tag.setInteger("fluidAmount", (Integer) this.properties[FLUID_AMOUNT]);
        tag.setString("fluidID", (String) this.properties[FLUID_ID]);
        tag.setInteger("tickForMelt", (Integer) this.properties[TICK_FOR_MELT]);
        tag.setInteger("numberToMelt", (Integer) this.properties[NUMBER_TO_MELT]);
        tag.setInteger("tickForInfuse", (Integer) this.properties[TICK_FOR_INFUSE]);
        tag.setInteger("numberToInfuse", (Integer) this.properties[NUMBER_TO_INFUSE]);
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tanks[0].getFluid() == null ? 0 : tanks[0].getFluid().amount) / tanks[0].getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public int getInfusingTimeRemainingScaled(int scaleFactor) {

        if ((Integer) this.properties[TICK_FOR_INFUSE] != 0 && tickInfusing != 0) {
            return tickInfusing * scaleFactor / (Integer) this.properties[TICK_FOR_INFUSE];
        } else return 0;

    }

    @SideOnly(Side.CLIENT)
    public int getMeltingTimeRemainingScaled(int scaleFactor) {

        if ((Integer) this.properties[TICK_FOR_MELT] != 0 && tickMelting != 0) {
            return tickMelting * scaleFactor / (Integer) this.properties[TICK_FOR_MELT];
        } else return 0;

    }

    public boolean isActive() {
        return isActive;
    }


    @Override
    public int getTankToShow() {
        return 0;
    }

    @Override
    public int getFluidLight() {
        return getFluidLight(getTankToShow());
    }

    @Override
    public FluidStack getFluid() {
        return getFluid(getTankToShow());
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return super.fill(0, from, resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return super.drain(0, from, resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return super.drain(0, from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (fluid != null && FluidInfuserRegister.isUsefulFluid(fluid)) {

            if ((from != EnumFacing.UP || from != EnumFacing.DOWN)) {
                return this.properties[FLUID_ID].equals("") || this.properties[FLUID_ID].equals(fluid.getName());
            }
        }
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return from == EnumFacing.DOWN;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return super.getTankInfo(0, from);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFluidInfuser(playerInventory, this);
    }

    @Override
    public String getGuiID() {

        return Reference.MOD_ID + ":" + ModBlocks.blockUpgCFluidInfuser.getName();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN)
            return output;
        return input;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return (index == MELT || index == INFUSE) && direction != EnumFacing.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == MELT || index == INFUSE) {
            return false;
        } else if (index == OUTPUT && direction == EnumFacing.DOWN) {
            return true;
        }

        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack == null) return true;
        else if (index == MELT) {

            if (inventory[MELT] != null && inventory[MELT].getItem() == stack.getItem()) {
                return true;
            } else if (inventory[MELT] != null && inventory[MELT].getItem() != stack.getItem()) return false;

            else if (inventory[MELT] == null) {
                boolean result = FluidInfuserRegister.canBeMelted(stack);
                if (result && inventory[INFUSE] != null) {
                    result = FluidInfuserRegister.canBeMelted(stack, inventory[INFUSE]);
                }
                return result;
            }
        } else if (index == INFUSE) {

            if (inventory[INFUSE] != null && inventory[INFUSE].getItem() == stack.getItem()) {
                return true;
            } else if (inventory[INFUSE] != null && inventory[INFUSE].getItem() != stack.getItem()) return false;
            else if (inventory[INFUSE] == null) {
                boolean result = FluidInfuserRegister.canBeInfused(stack);
                if (result && inventory[MELT] != null) {
                    result = FluidInfuserRegister.canBeInfused(stack, inventory[MELT]);
                }
                return result;
            }
        }

        return false;
    }


    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return tickMelting;
            case 1:
                return (Integer) properties[TICK_FOR_MELT];
            case 2:
                return tickInfusing;
            case 3:
                return (Integer) properties[TICK_FOR_INFUSE];
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {

        switch (id) {
            case 0:
                tickMelting = value;
                break;
            case 1:
                properties[TICK_FOR_MELT] = value;
                break;
            case 2:
                tickInfusing = value;
                break;
            case 3:
                properties[TICK_FOR_INFUSE] = value;
                break;
        }

    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    public int getCapacity() {
        return getCapacity(getTankToShow());
    }

    @Override
    public void update() {
        super.update();
        if (!worldObj.isRemote) {
            boolean oldIsActive = isActive;

            if (this.tanks[0] != null && this.tanks[0].getFluidAmount() > 0) {

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

    private boolean canStart() {

        if (inventory[MELT] == null || inventory[INFUSE] == null) {
            return false;
        }

        FluidInfuserRecipe recipe = FluidInfuserRegister.getFluidInfuserRecipe(inventory[MELT], inventory[INFUSE], getFluid(0));

        if (recipe != null) {

            if ((inventory[OUTPUT] != null) && (inventory[OUTPUT].getItem() != recipe.getResult().getItem()) && (inventory[OUTPUT].getItemDamage() == recipe.getResult().getItemDamage())) {
                return false;
            } else if (inventory[OUTPUT] != null && inventory[OUTPUT].stackSize >= 64) {
                return false;
            }

            FluidStack otherFluid = recipe.getFluidStack();

            int nToMelt = recipe.getNumberItemToMelt();
            int nToInfuse = recipe.getNumberItemToInfuse();

            boolean result = inventory[MELT].stackSize >= recipe.getNumberItemToMelt() && inventory[INFUSE].stackSize >= recipe.getNumberItemToInfuse(); // for prevent any possibles future bugs...

            if (!result) {
                return false;
            }

            FluidStack fluid = this.tanks[0].getFluid();

            if (fluid != null) {
                result = fluid.containsFluid(otherFluid) && fluid.amount >= (otherFluid.amount / recipe.getTicksToMelt());
            } else {
                return false;
            }

            if (result) {
                this.initProcess(otherFluid.amount, otherFluid.getFluid().getName(), recipe.getTicksToMelt(), nToMelt, recipe.getTicksToInfuse(), nToInfuse);
            }

            return result;
        }


        return false;
    }

    private void initProcess(int fluidAmount, String fluidID, int tToMelt, int nToMelt, int tToInfuse, int nToInfuse) {
        this.properties[FLUID_AMOUNT] = fluidAmount;
        this.properties[FLUID_ID] = fluidID;
        this.properties[TICK_FOR_MELT] = tToMelt;
        this.properties[TICK_FOR_INFUSE] = tToInfuse;

        this.properties[NUMBER_TO_MELT] = nToMelt;
        this.properties[NUMBER_TO_INFUSE] = nToInfuse;


    }

    private boolean canContinue() {
        ItemStack melt = inventory[MELT];
        ItemStack infuse = inventory[INFUSE];

        int nMelt = (Integer) this.properties[NUMBER_TO_MELT];
        int nInfuse = (Integer) this.properties[NUMBER_TO_INFUSE];
        int nFluid = (Integer) this.properties[FLUID_AMOUNT];
        int fluidAmount = this.tanks[0].getFluidAmount();

        return !((nMelt > 0 && melt == null) || (nInfuse > 0 && infuse == null) || fluidAmount < nFluid);

    }

    private void operating() {
        if (burning == 0 && tickMelting < (Integer) this.properties[TICK_FOR_MELT]) {
            FluidStack fluidBurned = this.tanks[0].drain(((Integer) this.properties[FLUID_AMOUNT] / (Integer) this.properties[TICK_FOR_MELT]), true);
            burning = fluidBurned.amount;
        }

        if (tickMelting < (Integer) this.properties[TICK_FOR_MELT]) {
            melting();
        } else if (tickInfusing < (Integer) this.properties[TICK_FOR_INFUSE]) {
            infusing();
        } else if (tickMelting == (Integer) properties[TICK_FOR_MELT] && tickInfusing == (Integer) properties[TICK_FOR_INFUSE]) {
            infuse();
        } else {
            LogHelper.error("This case should not be possible!!!");
        }


    }

    private void melting() {
        if ((burning > 0)) {
            burning = 0;
            tickMelting++;

            if ((tickMelting % ((Integer) this.properties[TICK_FOR_MELT] / (Integer) this.properties[NUMBER_TO_MELT])) == 0) {
                this.moveItem(MELT, MELT_P);
                this.properties[NUMBER_TO_MELT] = ((Integer) this.properties[NUMBER_TO_MELT]) - 1;
                burning = 0;
            }

        }
    }

    private void moveItem(int from, int to) {
        if (inventory[from] == null) {
            return;
        }
        if (inventory[from].stackSize <= 0) {
            inventory[from] = null;
            return;
        }

        if (inventory[to] == null) {
            inventory[to] = new ItemStack(inventory[from].getItem(), 1);
        } else {
            inventory[to].stackSize++;
        }

        inventory[from].stackSize--;

        if (inventory[from].stackSize <= 0) {
            inventory[from] = null;

        }


    }

    private void infusing() {
        tickInfusing++;
        if (inventory[INFUSE] != null && inventory[INFUSE_P] == null) {
            inventory[INFUSE_P] = new ItemStack(inventory[INFUSE].getItem(), (Integer) properties[NUMBER_TO_INFUSE], inventory[INFUSE].getItemDamage());
            inventory[INFUSE].stackSize -= (Integer) properties[NUMBER_TO_INFUSE];
            if (inventory[INFUSE].stackSize <= 0) {
                inventory[INFUSE] = null;
            }
            this.properties[NUMBER_TO_INFUSE] = 0;
        }
    }

    private void infuse() {

        FluidInfuserRecipe recipe = FluidInfuserRegister.getFluidInfuserRecipe(inventory[MELT_P], inventory[INFUSE_P], getFluid(0));

        if (recipe == null) {
            LogHelper.fatal("Fluid Infuser recipe is nul!!");
            return;
        }

        inventory[MELT_P] = null;
        inventory[INFUSE_P] = null;

        if (inventory[OUTPUT] == null) {

            inventory[OUTPUT] = new ItemStack(recipe.getResult().getItem(), recipe.getResult().stackSize, recipe.getResult().getItemDamage());

        } else {
            inventory[OUTPUT].stackSize += recipe.getResult().stackSize;
        }
        cleanProcess();
    }

    private void cleanProcess() {
        this.properties = new Object[]{0, "", 0, 0, 0, 0};
        this.tickMelting = 0;
        this.tickInfusing = 0;
        this.burning = 0;
        this.isOperating = false;

    }

}
