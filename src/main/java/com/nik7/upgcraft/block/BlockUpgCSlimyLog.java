package com.nik7.upgcraft.block;


import com.nik7.upgcraft.registry.CustomCraftingExperience;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockUpgCSlimyLog extends BlockUpgC implements CustomCraftingExperience {

    public BlockUpgCSlimyLog() {
        super(Material.wood, "SlimyLog");
        this.setHardness(2.0F);
        this.setStepSound(SoundType.WOOD);
        this.slipperiness = 1.05F; //0.98F ice
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 7;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 15;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.6F;
    }
}
