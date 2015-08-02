package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import com.nik7.upgcraft.util.ItemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCEnderHopper extends BlockUpgC implements ITileEntityProvider {

    private final Random rand = new Random();

    public BlockUpgCEnderHopper() {
        super(Material.rock);
        setBlockName(Names.Blocks.ENDER_HOPPER);
        this.setHardness(25.0F);
        this.setResistance(2000.0F);
        this.textureName = Texture.Blocks.ENDER_HOPPER;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        int newMeta = Facing.oppositeSide[side];

        if (newMeta == 1) {
            newMeta = 0;
        }

        boolean isNotGettingPower = !world.isBlockIndirectlyGettingPowered(x, y, z);
        newMeta = newMeta | (isNotGettingPower ? 0 : 8);


        return newMeta;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {

        UpgCtilientityEnderHopper te = (UpgCtilientityEnderHopper) world.getTileEntity(x, y, z);

        if (te != null && te.needsEnderPortalRender())
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(UpgradeCraft.instance, GUIs.ENDER_HOPPER.ordinal(), world, x, y, z);

        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        boolean isNotGettingPower = !world.isBlockIndirectlyGettingPowered(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        int dir = BlockUpgCBasicFluidHopper.getDirectionFromMetadata(meta);
        boolean flag = BlockUpgCBasicFluidHopper.isNotPowered(meta);
        if (isNotGettingPower != flag) {
            world.setBlockMetadataWithNotify(x, y, z, dir | (isNotGettingPower ? 0 : 8), 4);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtilientityEnderHopper te = (UpgCtilientityEnderHopper) world.getTileEntity(x, y, z);

        ItemStack itemStacks[] = te.getInventory();

        for (ItemStack itemStack : itemStacks) {
            ItemHelper.dropItems(world, x, y, z, itemStack, rand);
        }

        super.breakBlock(world, x, y, z, block, n);

    }

    public int getRenderType() {
        return Render.Ids.ENDER_HOPPER;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
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
    public MapColor getMapColor(int meta) {
        return MapColor.obsidianColor;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtilientityEnderHopper();
    }
}
