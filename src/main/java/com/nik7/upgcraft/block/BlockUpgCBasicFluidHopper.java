package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityFluidHopper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUpgCBasicFluidHopper extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon icon;


    public BlockUpgCBasicFluidHopper() {
        super(Material.iron);
        setBlockName("Basic" + Names.Blocks.FLUID_HOPPER);
        this.setHardness(3f);
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

        boolean isNotGettingPower = !world.isBlockIndirectlyGettingPowered(x, y, z);
        newMeta = newMeta | (isNotGettingPower ? 0 : 8);


        return newMeta;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return false;
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


    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    public int getRenderType() {
        return RenderIds.BASIC_FLUID_HOPPER;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon(Texture.Blocks.FLUID_BASIC_HOPPER);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return icon;
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
        return new UpgCtilientityFluidHopper();
    }

}
