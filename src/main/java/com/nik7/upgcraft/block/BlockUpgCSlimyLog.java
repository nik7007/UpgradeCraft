package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockUpgCSlimyLog extends BlockUpgC implements CustomCraftingExperience {

    public BlockUpgCSlimyLog() {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
        this.slipperiness = 1.05F; //0.98F ice
        this.setBlockName(Names.Blocks.SLIMY_LOG);
        this.setBlockTextureName(Texture.Blocks.SLIMY_LOG);
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 7;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 15;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.6F;
    }
}


