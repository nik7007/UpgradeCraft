package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;


public class ItemBlockIronFluidTank extends ItemBlockFluidTank {


    public ItemBlockIronFluidTank(Block block) {
        super(block, new UpgCEPFluidTank(Capacity.SMALL_TANK * 2));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1) {
            list.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }

        List hiddenInformation = new LinkedList();
        addFluidInformation(hiddenInformation, itemStack);

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }


}
