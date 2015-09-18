package com.nik7.upgcraft.block;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockUpgCFluidTankMold extends BlockUpgC {

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;

    public BlockUpgCFluidTankMold() {
        super(Material.clay);
        this.setHardness(2.8f);
        this.setStepSound(soundTypeStone);
        this.setBlockName(Names.Blocks.FLUID_TANK_MOLD);

        this.setHarvestLevel("shovel", 0, 0);
        this.setHarvestLevel("shovel", 0, 1);

        for (int meta = 2; meta < 6; meta++)
            this.setHarvestLevel("pickaxe", 1, meta);

    }


    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

        subItems.add(new ItemStack(this, 1, 0));
        subItems.add(new ItemStack(this, 1, 1));
        subItems.add(new ItemStack(this, 1, 2));
        subItems.add(new ItemStack(this, 1, 3));
        subItems.add(new ItemStack(this, 1, 4));
        subItems.add(new ItemStack(this, 1, 5));


    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        if (icons == null)
            icons = new IIcon[4];

        icons[0] = iconRegister.registerIcon(Texture.Blocks.FLUID_TANK_MOLD);
        icons[1] = iconRegister.registerIcon(Texture.Blocks.FLUID_TANK_MOLD + "_side");

        icons[2] = iconRegister.registerIcon(Texture.Blocks.FLUID_TANK_HARDENED_MOLD);
        icons[3] = iconRegister.registerIcon(Texture.Blocks.FLUID_TANK_HARDENED_MOLD + "_side");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (meta < 2) {
            if (side == 0 || side == 1)
                return icons[0];
            else return icons[1];
        } else if (side == 0 || side == 1)
            return icons[2];
        else return icons[3];

    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if (metadata == 0)
            ret.add(new ItemStack(this, 1, 0));
        else if (metadata == 1)
            ret.add(new ItemStack(this, 1, 1));
        else if (metadata == 4) {
            ret.add(new ItemStack(ModBlocks.blockUpgCIronFluidTank, 1, 0));
        } else if (metadata == 5) {
            ret.add(new ItemStack(ModBlocks.blockUpgCIronFluidTank, 1, 1));
        }

        return ret;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public MapColor getMapColor(int meta) {
        return MapColor.clayColor;
    }

}
