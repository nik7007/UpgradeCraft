package com.nik7.upgcraft.CreativeTab;

import com.nik7.upgcraft.block.BlockWoodenLiquidTank;
import com.nik7.upgcraft.block.InitBlocks;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab {

    public static final CreativeTabs UPGC_TAB = new CreativeTabs(Reference.MOD_NAME) {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(InitBlocks.blockWoodenLiquidTank);
        }


    };

}
