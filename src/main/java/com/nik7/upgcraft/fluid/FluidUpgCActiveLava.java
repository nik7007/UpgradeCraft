package com.nik7.upgcraft.fluid;


import com.nik7.upgcraft.reference.Reference;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class FluidUpgCActiveLava extends FluidUpgC implements FluidWithExtendedProperties<Integer> {

    public static final String ACTIVE_VALUE = "activeValue";
    public static final int MAX_ACTIVE_VALUE = 5800;

    public FluidUpgCActiveLava() {
        super("ActiveLava", new ResourceLocation(Reference.MOD_ID, "blocks/ActiveLava_still"), new ResourceLocation(Reference.MOD_ID, "blocks/ActiveLava_flow"));
        setLuminosity(8);
        setDensity(5000);
        setViscosity(8000);
        setTemperature(500 + 273);
        setRarity(EnumRarity.UNCOMMON);
    }

    public static int increaseActiveValue(FluidStack stack, int quantity) {

        if (stack.getFluid() instanceof FluidUpgCActiveLava) {
            FluidUpgCActiveLava activeLava = (FluidUpgCActiveLava) stack.getFluid();
            int actualValue = activeLava.getExtendedProperties(stack);
            int newValue = Math.min(MAX_ACTIVE_VALUE, actualValue + quantity);

            activeLava.createExtendedProperties(stack, newValue);

            return newValue - actualValue;
        }
        return 0;
    }

    public static int decreaseActiveValue(FluidStack stack, int quantity) {

        if (stack.getFluid() instanceof FluidUpgCActiveLava) {

            FluidUpgCActiveLava activeLava = (FluidUpgCActiveLava) stack.getFluid();
            int actualValue = activeLava.getExtendedProperties(stack);

            if (actualValue > quantity) {
                activeLava.createExtendedProperties(stack, actualValue - quantity);
                return quantity;
            } else {
                activeLava.createExtendedProperties(stack, 0);
                return actualValue;
            }
        }

        return 0;
    }

    public static boolean hasMaximumTemperature(FluidStack stack) {

        if (stack.getFluid() instanceof FluidUpgCActiveLava) {

            FluidUpgCActiveLava activeLava = (FluidUpgCActiveLava) stack.getFluid();

            return activeLava.getExtendedProperties(stack) == MAX_ACTIVE_VALUE;
        }

        return true;
    }

    private float getScalarAcV(FluidStack stack) {

        if (stack.tag != null)
            return (float) getExtendedProperties(stack) / (float) MAX_ACTIVE_VALUE;
        else return 0;
    }

    private boolean checkFluidStack(FluidStack fluidStack) {

        return fluidStack != null && fluidStack.getFluid() == this;
    }

    @Override
    public void createExtendedProperties(FluidStack fluidStack, Integer properties) {
        if (checkFluidStack(fluidStack)) {

            if (fluidStack.tag == null)
                fluidStack.tag = new NBTTagCompound();
            fluidStack.tag.setInteger(ACTIVE_VALUE, Math.min(properties, MAX_ACTIVE_VALUE));

        }

    }

    @Override
    public Integer getExtendedProperties(FluidStack fluidStack) {
        if (checkFluidStack(fluidStack)) {

            if (fluidStack.tag == null)
                createExtendedProperties(fluidStack, 0);
            else {
                return fluidStack.tag.getInteger(ACTIVE_VALUE);
            }

        }
        return 0;
    }

    @Override
    public boolean hasExtendedDrain(FluidStack fluidStack) {
        return false;
    }

    @Override
    public boolean hasToUseExtendedDrain(FluidStack fluidStack) {
        return fluidStack != null && fluidStack.tag != null;
    }

    @Override
    public boolean hasExtendedFill(FluidStack fluidStack) {
        return true;
    }

    @Override
    public boolean hasToUseExtendedFill(FluidStack fluidStack) {
        return fluidStack != null && fluidStack.tag != null;
    }

    @Override
    public FluidStack drain(IFluidTank fluidTank, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public int fill(IFluidTank fluidTank, FluidStack resource, boolean doFill) {
        if (fluidTank != null && checkFluidStack(resource)) {
            FluidStack me = fluidTank.getFluid();
            if (checkFluidStack(me)) {

                NBTTagCompound mTag = me.tag;
                me.tag = null;
                NBTTagCompound rTag = resource.tag;
                resource.tag = null;

                int result;

                if (!doFill) {

                    result = fluidTank.fill(resource, false);
                    me.tag = mTag;
                } else {
                    int mAmount = me.amount;
                    result = fluidTank.fill(resource, true);
                    me.tag = mTag;
                    resource.tag = rTag;

                    FluidStack actualFluid = fluidTank.getFluid();

                    int newActiveValue = Math.round((((float) getExtendedProperties(me) / (float) actualFluid.amount) * mAmount + ((float) getExtendedProperties(resource) / (float) actualFluid.amount) * result));
                    createExtendedProperties(actualFluid, newActiveValue);
                }
                resource.tag = rTag;

                return result;

            } else if (me == null) {
                return fluidTank.fill(resource, doFill);
            }
        }
        return 0;
    }

    @Override
    public void writeToByteBuf(FluidStack resource, PacketBuffer buf) {
        if (checkFluidStack(resource)) {
            int eP = getExtendedProperties(resource);
            buf.writeInt(eP);
        }
    }

    @Override
    public void readFromByteBuf(FluidStack resource, PacketBuffer buf) {

        if (checkFluidStack(resource)) {
            this.createExtendedProperties(resource, buf.readInt());
        }

    }

    @Override
    public int getLuminosity(FluidStack stack) {
        return (int) (getLuminosity() + (15 - getLuminosity()) * getScalarAcV(stack));
    }

    @Override
    public int getDensity(FluidStack stack) {
        return (int) (getDensity() - 2650 * getScalarAcV(stack));
    }

    @Override
    public int getTemperature(FluidStack stack) {
        return (int) (getTemperature() + (MAX_ACTIVE_VALUE - getTemperature()) * getScalarAcV(stack));
    }

    @Override
    public int getViscosity(FluidStack stack) {
        return (int) (getViscosity() - 4200 * getScalarAcV(stack));
    }
}