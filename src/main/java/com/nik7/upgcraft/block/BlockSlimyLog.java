package com.nik7.upgcraft.block;


import com.nik7.upgcraft.registry.ICraftingExperience;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSlimyLog extends BlockUpgC implements ICraftingExperience {

    public BlockSlimyLog() {
        super(Material.WOOD, "slimylog");
        this.setHardness(2.0F);
        this.slipperiness = 1.05F;
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
    public float getCraftingExperience(ItemStack item) {
        return 0.6f;
    }
}
