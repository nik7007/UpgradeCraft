package com.nik7.upgcraft.item;

import com.nik7.upgcraft.CreativeTab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemUpgC extends Item {

    public ItemUpgC() {
        super();
        this.setNoRepair();
        this.setCreativeTab(CreativeTab.UPGC_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    public static void addHiddenInformation(List list, List hiddenInformation) {

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.addAll(hiddenInformation);
        } else {
            list.add(EnumChatFormatting.DARK_GRAY + "[Shift]");
        }


    }

}
