package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCActiveMaker extends BlockUpgCContainerOrientable {

    private final Random rand = new Random();

    public BlockUpgCActiveMaker() {

        super(Material.iron);
        setBlockName(Names.Blocks.ACTIVE_MAKER);
        setStepSound(soundTypePiston);
        setHardness(25.0F);
        this.setStepSound(soundTypePiston);
        this.setBlockTextureName(Texture.Blocks.SLIMY_OBSIDIAN_BASE + 0);

    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    public int getRenderType() {
        return Render.Ids.FLUID_MACHINE;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityActiveMaker te = (UpgCtileentityActiveMaker) world.getTileEntity(x, y, z);

        for (int slot = 0; slot < te.getSizeInventory(); slot++) {
            dropItems(world, x, y, z, te.getStackInSlot(slot), rand);
        }
        super.breakBlock(world, x, y, z, block, n);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(UpgradeCraft.instance, GUIs.ACTIVE_MAKER.ordinal(), world, x, y, z);

        }
        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return ((UpgCtileentityInventoryFluidHandler) world.getTileEntity(x, y, z)).getFluidLightLevel(0);

    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityActiveMaker();
    }
}
