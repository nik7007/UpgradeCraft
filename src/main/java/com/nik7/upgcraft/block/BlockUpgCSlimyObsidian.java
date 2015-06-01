package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import net.minecraft.block.material.Material;

public class BlockUpgCSlimyObsidian extends BlockUpgC {

    public BlockUpgCSlimyObsidian() {

        super(Material.rock);
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockName(Names.Blocks.SLIMY_OBSIDIAN);
        this.setBlockTextureName(Texture.Blocks.SLIMY_OBSIDIAN);
        this.slipperiness = 1.08F;

    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }
}
