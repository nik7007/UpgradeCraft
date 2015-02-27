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


//TODO: add check for constraints
public class FluidInfuserRegister {

    private static final FluidInfuserRegister INSTANCE = new FluidInfuserRegister();

    private HashMap<InputItemStacks, FluidInfuserRecipe> inputsToAll = new HashMap<InputItemStacks, FluidInfuserRecipe>();

    private HashMap<Item, HashSet<FluidInfuserRecipe>> resultToAll = new HashMap<Item, HashSet<FluidInfuserRecipe>>();
    private HashMap<ItemOD, HashMap<ItemOD, FluidInfuserRecipe>> toMeltToAll = new HashMap<ItemOD, HashMap<ItemOD, FluidInfuserRecipe>>();
    private HashMap<ItemOD, HashMap<ItemOD, FluidInfuserRecipe>> toInfuseToAll = new HashMap<ItemOD, HashMap<ItemOD, FluidInfuserRecipe>>();
    private HashSet<Fluid> fluid = new HashSet<Fluid>();

    private FluidInfuserRegister() {

    }

    public static void addRecipe(FluidStack fluidStack, ItemStack result, ItemStack toMelt, int ticksToMelt, ItemStack toInfuse, int ticksToInfuse) {

        if (fluidStack != null && result != null && toMelt != null && toInfuse != null && ticksToInfuse > 0 && ticksToMelt > 0) {

            InputItemStacks inputs = new InputItemStacks(toMelt, toInfuse);
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
                    INSTANCE.toMeltToAll.get(toMeltOD).put(toInfuseOD, recipe);
                } else {
                    INSTANCE.toMeltToAll.put(toMeltOD, new HashMap<ItemOD, FluidInfuserRecipe>());
                    INSTANCE.toMeltToAll.get(toMeltOD).put(toInfuseOD, recipe);
                }


                if (INSTANCE.toInfuseToAll.containsKey(toInfuseOD)) {
                    INSTANCE.toInfuseToAll.get(toInfuseOD).put(toMeltOD, recipe);
                } else {
                    INSTANCE.toInfuseToAll.put(toInfuseOD, new HashMap<ItemOD, FluidInfuserRecipe>());
                    INSTANCE.toInfuseToAll.get(toInfuseOD).put(toMeltOD, recipe);
                }


            } else {
                LogHelper.error("FluidInfuserRegister: Recipe rejected - Duplicated inputs are not allowed!!");
            }
        }

    }


    public static ItemStack getResult(ItemStack toMelt, ItemStack toInfuse) {
        return INSTANCE.inputsToAll.get(new InputItemStacks(toMelt, toInfuse)).getResult();
    }

    public static FluidInfuserRecipe getFluidInfuserRecipe(ItemStack toMelt, ItemStack toInfuse) {
        if (canBeMelted(toMelt, toInfuse)) {
            ItemOD toMeltOD = new ItemOD(toMelt);
            ItemOD toInfuseOD = new ItemOD(toInfuse);

            return INSTANCE.toMeltToAll.get(toMeltOD).get(toInfuseOD);
        } else return null;
    }

    public static FluidStack getFluidStack(ItemStack toMelt, ItemStack toInfuse)
    {
        if (canBeMelted(toMelt, toInfuse)) {

            return getFluidInfuserRecipe(toMelt, toInfuse).getFluidStack();

        }
        else return null;

    }

    public static boolean isUsefulFluid(Fluid fluid) {
        return INSTANCE.fluid.contains(fluid);
    }

    public static boolean canBeMelted(ItemStack item) {
        return INSTANCE.toMeltToAll.containsKey(new ItemOD(item));
    }

    public static boolean canBeMelted(ItemStack toMelt, ItemStack toInfuse) {

        ItemOD toMeltOD = new ItemOD(toMelt);
        ItemOD toInfuseOD = new ItemOD(toInfuse);

        return INSTANCE.toMeltToAll.containsKey(toMeltOD) && INSTANCE.toMeltToAll.get(toMeltOD).containsKey(toInfuseOD);
    }

    public static boolean canBeInfused(ItemStack item) {
        return INSTANCE.toInfuseToAll.containsKey(new ItemOD(item));
    }

    public static boolean canBeInfused(ItemStack toInfuse, ItemStack toMelt) {

        ItemOD toMeltOD = new ItemOD(toMelt);
        ItemOD toInfuseOD = new ItemOD(toInfuse);

        return INSTANCE.toInfuseToAll.containsKey(toInfuseOD) && INSTANCE.toInfuseToAll.get(toInfuseOD).containsKey(toMeltOD);
    }

}
