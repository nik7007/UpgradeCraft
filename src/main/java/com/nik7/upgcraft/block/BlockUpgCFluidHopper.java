package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityFluidHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUpgCFluidHopper extends BlockUpgCBasicFluidHopper {

    public BlockUpgCFluidHopper() {

        super();
        setBlockName(Names.Blocks.FLUID_HOPPER);
        setBlockTextureName(Texture.Blocks.FLUID_HOPPER);

    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtilientityFluidHopper();
    }


}
