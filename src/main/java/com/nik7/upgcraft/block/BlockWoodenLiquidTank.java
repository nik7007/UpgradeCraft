package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenTankSmall;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;


public class BlockWoodenLiquidTank extends BlockUpgCTank {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private int flammability = 0;
    private int fireSpreadSpeed = 0;
    private int oldFlammability = 0;

    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODEN_LIQUID_TANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        this.setStepSound(soundTypeWood);
        this.setTickRandomly(true);
        this.flammability = 5;
        this.fireSpreadSpeed = 10;

        this.haveSubBlocks = true;

        this.capacity = Capacity.SMALL_TANK;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return fireSpreadSpeed;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (this.flammability > 0) {
            UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);

            if (entity.getTank().isToHot()) {
                spawnParticle(world, x, y, z, random, "smoke");
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        boolean canBurn = flammability > 0;
        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        boolean toHot = entity.getTank().isToHot();

        if (toHot && canBurn) {

            if (oldFlammability == 0)
                oldFlammability = flammability;
            flammability = 75;

            if (!setInFire(world, x, y, z))
                setInFireNeighbors(world, x, y, z);

        }

        if ((!toHot || !canBurn) && oldFlammability > 0)
            flammability = oldFlammability;

    }

    private boolean setInFireNeighbors(World world, int x, int y, int z) {

        if (world.getBlock(x, y + 1, z).isFlammable(world, x, y + 1, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y + 1, z);

        } else if (world.getBlock(x, y, z + 1).isFlammable(world, x, y, z + 1, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y, z + 1);

        } else if (world.getBlock(x, y, z - 1).isFlammable(world, x, y, z - 1, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y, z - 1);

        } else if (world.getBlock(x + 1, y, z).isFlammable(world, x + 1, y, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x + 1, y, z);

        } else if (world.getBlock(x - 1, y, z).isFlammable(world, x - 1, y, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x - 1, y, z);

        } else if (world.getBlock(x, y - 1, z).isFlammable(world, x, y - 1, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y - 1, z);

        }

        return false;
    }

    private boolean setInFire(World world, int x, int y, int z) {

        if (world.isAirBlock(x, y + 1, z)) {
            world.setBlock(x, y + 1, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y - 1, z)) {
            world.setBlock(x, y - 1, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y, z + 1)) {
            world.setBlock(x, y, z + 1, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y, z - 1)) {
            world.setBlock(x, y, z - 1, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x + 1, y, z)) {
            world.setBlock(x + 1, y, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x - 1, y, z)) {
            world.setBlock(x - 1, y, z, Blocks.fire);
            return true;
        }

        return false;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon(Texture.Blocks.WOODEN_LIQUID_TANK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return icon;
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
        return 8;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityWoodenTankSmall();
    }


}



