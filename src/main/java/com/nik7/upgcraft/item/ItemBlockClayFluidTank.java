package com.nik7.upgcraft.item;

import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockClayFluidTank extends ItemBlock {

    public ItemBlockClayFluidTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = "Hardened";
        if (stack.getItemDamage() >= 2)
            return this.getUnlocalizedName() + "." + s;
        else return this.getUnlocalizedName();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1 || metaData == 3) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }
    }
}
