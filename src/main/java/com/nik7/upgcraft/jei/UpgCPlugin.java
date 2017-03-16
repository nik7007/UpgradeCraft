package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceCategory;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceHandler;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceMaker;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserCategory;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserHandler;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserMaker;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class UpgCPlugin extends BlankModPlugin {

    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelper = registry.getJeiHelpers();
        registry.addRecipeCategories(new FluidFurnaceCategory(), new FluidInfuserCategory());
        registry.addRecipeHandlers(new FluidFurnaceHandler(), new FluidInfuserHandler());

        registry.addRecipeClickArea(GuiFluidFurnace.class, 78, 32, 28, 23, ModBlocks.blockFluidFurnace.getUnlocalizedName());
        registry.addRecipeClickArea(GuiFluidInfuser.class, 94, 38, 34, 10, ModBlocks.blockFluidInfuser.getUnlocalizedName());

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidFurnace.class, ModBlocks.blockFluidFurnace.getUnlocalizedName(), 0, 1, 2, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidInfuser.class, ModBlocks.blockFluidInfuser.getUnlocalizedName(), 0, 2, 3, 36);

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockFluidFurnace), ModBlocks.blockFluidFurnace.getUnlocalizedName());
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockFluidInfuser), ModBlocks.blockFluidInfuser.getUnlocalizedName());

        registry.addRecipes(FluidFurnaceMaker.getRecipes());
        registry.addRecipes(FluidInfuserMaker.getRecipes());
    }
}
