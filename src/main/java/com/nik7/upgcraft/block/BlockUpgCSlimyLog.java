package com.nik7.upgcraft.block;


import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUpgCSlimyLog extends BlockUpgC implements CustomCraftingExperience {

    private static final String name = "SlimyLog";

    public BlockUpgCSlimyLog() {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
        this.slipperiness = 1.05F; //0.98F ice
        this.setUnlocalizedName(name);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 7;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 15;
    }

    public String getName() {
        return name;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.6F;
    }
}
