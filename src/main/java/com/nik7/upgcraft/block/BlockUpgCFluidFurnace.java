package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUpgCFluidFurnace extends BlockUpgCContainerOrientable{

    public BlockUpgCFluidFurnace() {
        super(Material.iron, "FluidFurnace");
        setStepSound(soundTypePiston);
        setHardness(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtileentityFluidFurnace();
    }
}
