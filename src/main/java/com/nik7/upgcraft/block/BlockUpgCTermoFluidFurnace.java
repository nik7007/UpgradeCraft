package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTermoFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCTermoFluidFurnace extends BlockUpgCContainerOrientable {

    private final Random rand = new Random();

    public BlockUpgCTermoFluidFurnace() {
        super(Material.iron);
        setBlockName(Names.Blocks.TERMO_FLUID_FURNACE);
        setStepSound(soundTypePiston);
        setHardness(25.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockTextureName(Texture.Blocks.SLIMY_OBSIDIAN_BASE + 0);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    /*public int getRenderType() {
        return Render.Ids.FLUID_MACHINE;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    */

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        UpgCtileentityTermoFluidFurnace te = (UpgCtileentityTermoFluidFurnace) world.getTileEntity(x, y, z);
        if (te.isActive) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.2F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            switch (l) {
                case 4:
                    world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 5:
                    world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 2:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    break;
                case 3:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityTermoFluidFurnace te = (UpgCtileentityTermoFluidFurnace) world.getTileEntity(x, y, z);

        for (int slot = 0; slot < te.getSizeInventory(); slot++) {
            dropItems(world, x, y, z, te.getStackInSlot(slot), rand);
        }
        super.breakBlock(world, x, y, z, block, n);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityTermoFluidFurnace();
    }
}
