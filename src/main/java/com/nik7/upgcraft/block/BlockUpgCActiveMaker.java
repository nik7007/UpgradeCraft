package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCActiveMaker extends BlockUpgCContainerOrientable {

    private final Random rand = new Random();

    public BlockUpgCActiveMaker() {

        super(Material.iron);
        setBlockName(Names.Blocks.ACTIVE_MAKER);
        setStepSound(soundTypePiston);
        setHardness(25.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockTextureName(Texture.Blocks.SLIMY_OBSIDIAN_BASE + 0);

    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityActiveMaker te = (UpgCtileentityActiveMaker) world.getTileEntity(x, y, z);

        for (int slot = 0; slot < te.getSizeInventory(); slot++) {
            dropItems(world, x, y, z, te.getStackInSlot(slot), rand);
        }
        super.breakBlock(world, x, y, z, block, n);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityActiveMaker();
    }
}
