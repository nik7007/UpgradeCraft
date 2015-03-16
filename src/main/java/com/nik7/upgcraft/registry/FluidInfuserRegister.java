package com.nik7.upgcraft.registry;


import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuser.InputItemStacks;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class FluidInfuserRegister {

    private static final FluidInfuserRegister INSTANCE = new FluidInfuserRegister();

    private HashMap<InputItemStacks, FluidInfuserRecipe> inputsToAll = new HashMap<InputItemStacks, FluidInfuserRecipe>();

    private HashMap<Item, HashSet<FluidInfuserRecipe>> resultToAll = new HashMap<Item, HashSet<FluidInfuserRecipe>>();
    private HashMap<ItemOD, HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>> toMeltToAll = new HashMap<ItemOD, HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>>();
    private HashMap<ItemOD, HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>> toInfuseToAll = new HashMap<ItemOD, HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>>();
    private HashSet<Fluid> fluid = new HashSet<Fluid>();

    private FluidInfuserRegister() {

    }

    public static void addRecipe(FluidStack fluidStack, ItemStack result, ItemStack toMelt, int ticksToMelt, ItemStack toInfuse, int ticksToInfuse) {

        if (fluidStack != null && result != null && toMelt != null && toInfuse != null && ticksToInfuse > 0 && ticksToMelt > 0) {

            if (checkConstraints(fluidStack, toMelt, ticksToMelt)) {

                InputItemStacks inputs = new InputItemStacks(toMelt, toInfuse, fluidStack);
                FluidInfuserRecipe recipe = new FluidInfuserRecipe(inputs, fluidStack, result, ticksToInfuse, ticksToMelt);

                if (!INSTANCE.inputsToAll.containsKey(inputs)) {

                    INSTANCE.inputsToAll.put(inputs, recipe);

                    if (!INSTANCE.fluid.contains(fluidStack)) {
                        INSTANCE.fluid.add(fluidStack.getFluid());
                    }

                    if (INSTANCE.resultToAll.containsKey(result)) {
                        (INSTANCE.resultToAll.get(result)).add(recipe);
                    } else {
                        INSTANCE.resultToAll.put(result.getItem(), new HashSet<FluidInfuserRecipe>());
                        (INSTANCE.resultToAll.get(result.getItem())).add(recipe);
                    }

                    ItemOD toMeltOD = new ItemOD(toMelt);
                    ItemOD toInfuseOD = new ItemOD(toInfuse);

                    if (INSTANCE.toMeltToAll.containsKey(toMeltOD)) {

                        HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>> a = INSTANCE.toMeltToAll.get(toMeltOD);

                        if (a.containsKey(toInfuseOD)) {
                            a.get(toInfuseOD).put(fluidStack.getFluid(), recipe);
                        } else {
                            a.put(toInfuseOD, new HashMap<Fluid, FluidInfuserRecipe>());
                            a.get(toInfuseOD).put(fluidStack.getFluid(), recipe);
                        }


                    } else {

                        HashMap<Fluid, FluidInfuserRecipe> map = new HashMap<Fluid, FluidInfuserRecipe>();
                        map.put(fluidStack.getFluid(), recipe);


                        INSTANCE.toMeltToAll.put(toMeltOD, new HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>());

                        boolean c = INSTANCE.toMeltToAll.containsKey(toMeltOD);

                        HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>> a = INSTANCE.toMeltToAll.get(toMeltOD);
                        a.put(toInfuseOD, map);

                    }


                    if (INSTANCE.toInfuseToAll.containsKey(toInfuseOD)) {

                        HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>> a = INSTANCE.toInfuseToAll.get(toInfuseOD);

                        if (a.containsKey(toMeltOD)) {
                            a.get(toMeltOD).put(fluidStack.getFluid(), recipe);
                        } else {
                            a.put(toMeltOD, new HashMap<Fluid, FluidInfuserRecipe>());
                            a.get(toMeltOD).put(fluidStack.getFluid(), recipe);
                        }


                    } else {

                        HashMap<Fluid, FluidInfuserRecipe> map = new HashMap<Fluid, FluidInfuserRecipe>();
                        map.put(fluidStack.getFluid(), recipe);

                        INSTANCE.toInfuseToAll.put(toInfuseOD, new HashMap<ItemOD, HashMap<Fluid, FluidInfuserRecipe>>());
                        INSTANCE.toInfuseToAll.get(toInfuseOD).put(toMeltOD, map);
                    }


                } else {
                    LogHelper.error("FluidInfuserRegister: Recipe rejected - Duplicated inputs are not allowed!!");
                }
            } else {
                LogHelper.error("FluidInfuserRegister: Impossible to insert this recipe - Violate integrity constraints!");
            }
        }

    }


    public static ItemStack getResult(ItemStack toMelt, ItemStack toInfuse, FluidStack fluidStack) {

        InputItemStacks inputItemStacks = new InputItemStacks(toMelt, toInfuse, fluidStack);
        if (INSTANCE.inputsToAll.containsKey(inputItemStacks)) {
            return INSTANCE.inputsToAll.get(inputItemStacks).getResult();
        } else {
            LogHelper.error("FluidInfuserRegister: This recipe doesn't exist!");
            return null;
        }
    }

    public static FluidInfuserRecipe getFluidInfuserRecipe(ItemStack toMelt, ItemStack toInfuse, FluidStack fluidStack) {
        if (canBeMelted(toMelt, toInfuse)) {
            ItemOD toMeltOD = new ItemOD(toMelt);
            ItemOD toInfuseOD = new ItemOD(toInfuse);

            return INSTANCE.toMeltToAll.get(toMeltOD).get(toInfuseOD).get(fluidStack.getFluid());
        } else return null;
    }

    public static Set getFluidSetStack(ItemStack toMelt, ItemStack toInfuse) {
        if (canBeMelted(toMelt, toInfuse)) {
            ItemOD toMeltOD = new ItemOD(toMelt);
            ItemOD toInfuseOD = new ItemOD(toInfuse);

            return INSTANCE.toMeltToAll.get(toMeltOD).get(toInfuseOD).keySet();
        } else return null;
    }

    public static boolean isUsefulFluid(Fluid fluid) {
        return INSTANCE.fluid.contains(fluid);
    }

    public static boolean canBeMelted(ItemStack item) {
        return INSTANCE.toMeltToAll.containsKey(new ItemOD(item));
    }

    public static boolean canBeMelted(ItemStack toMelt, ItemStack toInfuse) {

        if (toInfuse == null) {
            return canBeMelted(toMelt);
        }

        ItemOD toMeltOD = new ItemOD(toMelt);
        ItemOD toInfuseOD = new ItemOD(toInfuse);

        return INSTANCE.toMeltToAll.containsKey(toMeltOD) && INSTANCE.toMeltToAll.get(toMeltOD).containsKey(toInfuseOD);
    }

    public static boolean canBeInfused(ItemStack item) {
        return INSTANCE.toInfuseToAll.containsKey(new ItemOD(item));
    }

    public static boolean canBeInfused(ItemStack toInfuse, ItemStack toMelt) {

        if (toMelt == null) {
            return canBeInfused(toInfuse);
        }

        ItemOD toMeltOD = new ItemOD(toMelt);
        ItemOD toInfuseOD = new ItemOD(toInfuse);

        return INSTANCE.toInfuseToAll.containsKey(toInfuseOD) && INSTANCE.toInfuseToAll.get(toInfuseOD).containsKey(toMeltOD);
    }

    private static boolean checkConstraints(FluidStack fluidStack, ItemStack toMelt, int ticksToMelt) {

        return fluidStack.amount % ticksToMelt == 0 && ticksToMelt % toMelt.stackSize == 0;
    }

}
