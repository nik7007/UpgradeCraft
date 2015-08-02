package com.nik7.upgcraft.block;


import com.nik7.upgcraft.entity.player.ExtendedPlayerUpgC;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityEnderTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;

import java.util.Random;

public class BlockUpgCEnderTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public BlockUpgCEnderTank() {
        super(Material.rock);
        this.setHardness(22.5F);
        this.setResistance(1000.0F);
        this.setStepSound(soundTypePiston);
        this.capacity = Capacity.SMALL_TANK;
        this.haveSubBlocks = false;
        this.setBlockName(Names.Blocks.ENDER_TANK);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
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
        this.icon = iconRegister.registerIcon(Texture.Blocks.SLIMY_OBSIDIAN_BASE + 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public int getRenderType() {
        return Render.Ids.FLUID_TANK;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {

        for (int l = 0; l < 3; ++l) {
            double d1 = (double) ((float) y + random.nextFloat());
            double d3;
            double d4;
            double d5;
            int i1 = random.nextInt(2) * 2 - 1;
            int j1 = random.nextInt(2) * 2 - 1;
            d4 = ((double) random.nextFloat() - 0.5D) * 0.125D;
            double d2 = (double) z + 0.5D + 0.25D * (double) j1;
            d5 = (double) (random.nextFloat() * 1.0F * (float) j1);
            double d0 = (double) x + 0.5D + 0.25D * (double) i1;
            d3 = (double) (random.nextFloat() * 1.0F * (float) i1);
            world.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
        }

    }

    @Override
    protected void handleContainerClick(World world, int x, int y, int z, EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {
        if (FluidContainerRegistry.isBucket(equippedItemStack)) {
            entity.setTank(ExtendedPlayerUpgC.getUpgCEnderTank(player));
            if (FluidContainerRegistry.isEmptyContainer(equippedItemStack)) {
                super.fillBucketFromTank(world, x, y, z, player, entity, equippedItemStack);
            } else {
                super.drainBucketIntoTank(player, entity, equippedItemStack);

            }
            entity.setTank(new UpgCTank(0));
        }

    }

    public Item getItemDropped(int i, Random random, int j) {
        return Item.getItemFromBlock(ModBlocks.blockUpgCSlimyObsidian);
    }

    public int quantityDropped(Random random) {
        return 8;
    }

    protected boolean canSilkHarvest() {
        return true;
    }


    @Override
    public MapColor getMapColor(int meta) {
        return MapColor.obsidianColor;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityEnderTank();
    }

}
