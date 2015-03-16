package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankClay;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockClayFluidTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon icon;
    private IIcon iconHarden;

    public BlockClayFluidTank() {
        super(Material.clay);
        this.setBlockName(Names.Blocks.CLAY_LIQUID_TANK);
        this.setHardness(2.8f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        this.setStepSound(soundTypeStone);

        this.capacity = Capacity.SMALL_TANK;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

        subItems.add(new ItemStack(this, 1, 0));
        subItems.add(new ItemStack(this, 1, 1));
        subItems.add(new ItemStack(this, 1, 2));
        subItems.add(new ItemStack(this, 1, 3));


    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof UpgCtileentityTankClay) {
            int meta = entity.getBlockMetadata();

            if (meta < 2) {
                boolean hot = ((UpgCtileentityTankClay) entity).getTank().isToHot();
                if (hot) {
                    spawnParticle(world, x, y, z, random, "smoke");
                }
            }
        }
    }

    @Override
    public int getRenderType() {
        return RenderIds.FLUID_TANK;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon(Texture.Blocks.CLAY_LIQUID_TANK);
        this.iconHarden = iconRegister.registerIcon(Texture.Blocks.HARDENED_CLAY_LIQUID_TANK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 2)
            return icon;
        else
            return iconHarden;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        if (world.getBlock(x, y - 1, z) == this)
            if (world.getBlock(x, y - 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this)
            if (world.getBlock(x, y + 2, z) == this)
                return false;
        return !(world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this);

    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1 || meta == 3) {
            return ((UpgCtileentityTank) world.getTileEntity(x, y, z)).getFluidLightLevel();
        } else
            return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityTankClay();
    }
}
