package com.nik7.upgcraft.item;

import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class ItemUpgC extends Item {

    protected final String name;

    public ItemUpgC(String name) {
        super();
        this.setNoRepair();
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.setUnlocalizedName(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Reference.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    public static void addHiddenInformation(List<String> list, List<String> hiddenInformation) {

        if (!hiddenInformation.isEmpty())
            if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add("");
                list.addAll(hiddenInformation);
            } else {
                list.add(EnumChatFormatting.DARK_GRAY + "[Shift]");
            }


    }

}
