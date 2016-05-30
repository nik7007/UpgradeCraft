package com.nik7.upgcraft.redstone;

import net.minecraft.item.ItemStack;

public interface IItemRedLogicComponent {

    void setRotation(ItemStack itemStack, short rotation);

    short getRotation(ItemStack itemStack);

    ExpressionType getExpressionType();
}
