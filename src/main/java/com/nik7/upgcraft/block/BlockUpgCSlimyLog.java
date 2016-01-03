package com.nik7.upgcraft.block;


import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUpgCSlimyLog extends BlockUpgC implements CustomCraftingExperience {

    private static final String name = "SlimyLog";

    public BlockUpgCSlimyLog() {
        super(Material.wood);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
        this.slipperiness = 1.05F; //0.98F ice
        this.setUnlocalizedName(name);
        GameRegistry.registerBlock(this, name);

    }

    public String getName() {
        return name;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.6F;
    }
}
