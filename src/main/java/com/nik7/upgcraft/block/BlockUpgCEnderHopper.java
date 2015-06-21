package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUpgCEnderHopper extends BlockUpgC implements ITileEntityProvider {

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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        boolean isNotGettingPower = !world.isBlockIndirectlyGettingPowered(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        int dir = BlockUpgCBasicFluidHopper.getDirectionFromMetadata(meta);
        boolean flag = BlockUpgCBasicFluidHopper.isNotPowered(meta);
        if (isNotGettingPower != flag) {
            world.setBlockMetadataWithNotify(x, y, z, dir | (isNotGettingPower ? 0 : 8), 4);
        }
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
