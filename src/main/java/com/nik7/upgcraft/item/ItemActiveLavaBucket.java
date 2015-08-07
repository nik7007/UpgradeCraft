package com.nik7.upgcraft.item;


import com.nik7.upgcraft.CreativeTab.CreativeTab;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

public class ItemActiveLavaBucket extends ItemBucket {

    public ItemActiveLavaBucket() {
        super(ModBlocks.blockActiveLava);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.setUnlocalizedName(Names.Items.ACTIVE_LAVA_BUCKET);
        this.setTextureName(Texture.Items.ACTIVE_LAVA_BUCKET);
        this.setContainerItem(Items.bucket);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
}
