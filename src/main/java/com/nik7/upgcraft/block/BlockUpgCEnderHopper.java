package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import com.nik7.upgcraft.util.ItemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtilientityEnderHopper();
    }
}
