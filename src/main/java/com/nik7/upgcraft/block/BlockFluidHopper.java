package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCraftFluidHopper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockFluidHopper extends BlockUpgCTank {


    public BlockFluidHopper() {
        super(Material.iron);
        setBlockName("Basic" + Names.Blocks.FLUID_HOPPER);
    }

    public static int getDirectionFromMetadata(int meta) {
        return meta & 7;
    }

    public static boolean isNotPowered(int meta) {
        return (meta & 8) != 8;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        int newMeta = Facing.oppositeSide[side];

        if (newMeta == 1) {
            newMeta = 0;
        }

        return newMeta;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        boolean isNotGettingPower = !world.isBlockIndirectlyGettingPowered(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        int dir = getDirectionFromMetadata(meta);
        boolean flag = isNotPowered(meta);
        if (isNotGettingPower != flag) {
            world.setBlockMetadataWithNotify(x, y, z, dir | (isNotGettingPower ? 0 : 8), 4);
        }
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
        return new UpgCraftFluidHopper();
    }
}
