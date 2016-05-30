package com.nik7.upgcraft.item;

import com.nik7.upgcraft.redstone.ExpressionType;
import com.nik7.upgcraft.redstone.IItemRedLogicComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUpgCRedLogicComponent extends ItemUpgC implements IItemRedLogicComponent {

    private final ExpressionType expressionType;

    public ItemUpgCRedLogicComponent(String name, ExpressionType expressionType) {
        super(name);
        this.expressionType = expressionType;
    }

    @Override
    public void setRotation(ItemStack itemStack, short rotation) {

        if (itemStack != null) {

            if (!itemStack.hasTagCompound())
                itemStack.setTagCompound(new NBTTagCompound());

            itemStack.getTagCompound().setShort("rotation", rotation);
        }
    }

    @Override
    public short getRotation(ItemStack itemStack) {

        if (itemStack != null) {
            if (itemStack.hasTagCompound()) {
                return itemStack.getTagCompound().getShort("rotation");
            }
        }

        return 0;
    }

    @Override
    public ExpressionType getExpressionType() {
        return this.expressionType;
    }
}
