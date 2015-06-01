package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockUpgCSlimyObsidian extends BlockUpgC {

    @SideOnly(Side.CLIENT)
    public static IIcon[] icons = new IIcon[16];

    public BlockUpgCSlimyObsidian() {

        super(Material.rock);
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockName(Names.Blocks.SLIMY_OBSIDIAN);
        this.slipperiness = 1.08F;

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < 16; i++)
            icons[i] = iconRegister.registerIcon(Texture.Blocks.SLIMY_OBSIDIAN_BASE + i);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        Block u = null, d = null, r = null, l = null;

        if (side == 0 || side == 1) {
            u = world.getBlock(x, y, z - 1);
            d = world.getBlock(x, y, z + 1);
            r = world.getBlock(x + 1, y, z);
            l = world.getBlock(x - 1, y, z);
        }
        if (side == 2 || side == 3) {
            u = world.getBlock(x, y + 1, z);
            d = world.getBlock(x, y - 1, z);
            r = world.getBlock(x + (side == 2 ? -1 : +1), y, z);
            l = world.getBlock(x + (side == 2 ? +1 : -1), y, z);
        }
        if (side == 4 || side == 5) {
            u = world.getBlock(x, y + 1, z);
            d = world.getBlock(x, y - 1, z);
            r = world.getBlock(x, y, z + (side == 5 ? -1 : +1));
            l = world.getBlock(x, y, z + (side == 5 ? +1 : -1));
        }

        if (u != null && d != null && r != null && l != null) {
            if (u == this && d == this && r == this && l == this)
                return icons[11];
            if (u == this && d == this && r != this && l == this)
                return icons[10];
            if (u == this && d != this && r == this && l == this)
                return icons[9];
            if (u == this && d == this && r == this && l != this)
                return icons[8];
            if (u != this && d == this && r == this && l == this)
                return icons[7];
            if (u == this && d == this && r != this && l != this)
                return icons[12];
            if (u != this && d != this && r == this && l == this)
                return icons[13];
            if (u == this && r == this && l != this && d != this)
                return icons[5];
            if (u != this && r != this && l == this && d == this)
                return icons[6];
            if (u == this && l == this && d != this && r != this)
                return icons[14];
            if (r == this && d == this && u != this && l != this)
                return icons[15];
            if (r == this && d != this && l != this && u != this)
                return icons[1];
            if (u == this && r != this && d != this && l != this)
                return icons[2];
            if (l == this && r != this && d != this && u != this)
                return icons[3];
            if (d == this && u != this && r != this && l != this)
                return icons[4];
            return icons[0];
        }


        return icons[0];

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[0];
    }


    @Override
    public int getMobilityFlag() {
        return 2;
    }
}
