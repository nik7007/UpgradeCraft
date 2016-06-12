package com.nik7.upgcraft.item;


import com.nik7.upgcraft.redstone.ExpressionType;
import com.nik7.upgcraft.redstone.IItemRedLogicComponent;
import com.nik7.upgcraft.redstone.RedstoneComplexLogicElement;
import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBlockRedLogicComponent extends ItemBlock implements IItemRedLogicComponent {

    public ItemBlockRedLogicComponent(Block block) {
        super(block);
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
        return ExpressionType.CUSTOM;
    }

    public void setRedstoneComplexLogicElement(ItemStack itemStack, RedstoneComplexLogicElement element) {

        if (itemStack != null && element != null) {

            if (!itemStack.hasTagCompound())
                itemStack.setTagCompound(new NBTTagCompound());
            NBTTagCompound tagCompound = new NBTTagCompound();
            NBTTagHelper.writeRedstoneLogicElement(element, tagCompound);
            itemStack.getTagCompound().setTag("RedstoneComplexLogicElement", tagCompound);

        }

    }

    public RedstoneComplexLogicElement getRedstoneComplexLogicElement(ItemStack itemStack) {

        if (itemStack != null && itemStack.hasTagCompound()) {

            NBTTagCompound tagCompound = itemStack.getTagCompound().getCompoundTag("RedstoneComplexLogicElement");

            if (tagCompound != null) {
                return (RedstoneComplexLogicElement) NBTTagHelper.readRedstoneLogicElement(tagCompound);
            }
        }
        return null;
    }
}
