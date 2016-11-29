package com.nik7.upgcraft.creativetab;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTab {

    public static final CreativeTabs UPGC_TAB = new CreativeTabs(Reference.MOD_NAME) {

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ModBlocks.woodenFluidTank, 1, 1);
        }

    };
}
