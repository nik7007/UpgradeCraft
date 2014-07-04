package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockWoodenLiquidTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconSideD;
    @SideOnly(Side.CLIENT)
    private IIcon iconSideU;


    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODEN_LIQUID_TANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f); //chest block bounds
        this.setStepSound(soundTypeWood);
        this.setTickRandomly(true);
        this.flammability = 5;
        this.fireSpreadSpeed = 10;
        this.setBlockTextureName(Texture.WOODEN_LIQUID_TANK);

        capacity = Capacity.SMALL_WOODEN_TANK;

    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return fireSpreadSpeed;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.iconSide = iconRegister.registerIcon(Texture.WOODEN_LIQUID_TANK);
        this.iconTop = iconRegister.registerIcon(Texture.WOODEN_LIQUID_TANK + "_top");
        this.iconSideD = iconRegister.registerIcon(Texture.WOODEN_LIQUID_TANK + "_sideD");
        this.iconSideU = iconRegister.registerIcon(Texture.WOODEN_LIQUID_TANK + "_sideU");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (side == 0 || side == 1)
            return iconTop;
        if (meta == 5)
            return iconSideD;
        if (meta == 6)
            return iconSideU;

        return iconSide;
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
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        meta = 4;
        //world.setBlock(x,y,z,this,meta,4);
        if (world.getBlock(x, y + 1, z) == this) {
            meta = 5;
            world.setBlockMetadataWithNotify(x, y + 1, z, 6, 3);
        }
        if (world.getBlock(x, y - 1, z) == this) {
            meta = 6;
            world.setBlockMetadataWithNotify(x, y - 1, z, 5, 3);
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 3);
        return meta;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        onBlockPlaced(world, x, y, z, 0, x, y, z, 10);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

        if (world.getBlock(x, y + 1, z) == this) {
            if (world.getBlockMetadata(x, y, z) == 5)
                this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);

        }
        if (world.getBlock(x, y + 1, z) != this) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);

        }

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
    public void onBlockAdded(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityTankSmall();
    }


}




