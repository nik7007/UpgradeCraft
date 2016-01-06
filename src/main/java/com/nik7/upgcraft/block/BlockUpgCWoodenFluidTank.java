package com.nik7.upgcraft.block;


import com.nik7.upgcraft.item.ItemBlockWoodenFluidTank;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUpgCWoodenFluidTank extends BlockUpgCTank {


    public BlockUpgCWoodenFluidTank() {
        super(Material.wood, Capacity.SMALL_TANK, "WoodenTank", ItemBlockWoodenFluidTank.class);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityWoodenFluidTank();
    }
}
