package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockWoodenLiquidTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconSide;

    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODEN_LIQUID_TANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (side == 0 || side == 1)
            return iconTop;

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



