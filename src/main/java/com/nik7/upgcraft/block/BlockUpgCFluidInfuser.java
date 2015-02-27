package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCFluidInfuser extends BlockUpgCContainerOrientable {

    private final Random rand = new Random();

    public BlockUpgCFluidInfuser() {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_INFUSE);
        this.setBlockTextureName("iron_block");
        setStepSound(soundTypePiston);
        setHardness(5.2F);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return RenderIds.FLUID_MACHINE;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        /*if (!world.isRemote) {
            player.openGui(UpgradeCraft.instance, GUIs.FLUID_FURNACE.ordinal(), world, x, y, z);

        }
        return true;*/
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        UpgCtileentityFluidInfuser te = (UpgCtileentityFluidInfuser) world.getTileEntity(x, y, z);
        if (te.isActive) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.2F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            switch (l) {
                case 4:
                    world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 5:
                    world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 2:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    break;
                case 3:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityFluidInfuser te = (UpgCtileentityFluidInfuser) world.getTileEntity(x, y, z);

        ItemStack itemStackIn = te.getStackInSlot(UpgCtileentityFluidInfuser.INFUSE);
        ItemStack itemStackInP = te.getStackInSlot(UpgCtileentityFluidInfuser.INFUSE_P);
        ItemStack itemStackMelt = te.getStackInSlot(UpgCtileentityFluidInfuser.MELT);
        ItemStack itemStackOut = te.getStackInSlot(UpgCtileentityFluidInfuser.OUTPUT);

        dropItems(world, x, y, z, itemStackIn, rand);
        dropItems(world, x, y, z, itemStackInP, rand);
        dropItems(world, x, y, z, itemStackMelt, rand);
        dropItems(world, x, y, z, itemStackOut, rand);

        super.breakBlock(world, x, y, z, block, n);

    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1) {
            return ((UpgCtileentityInventoryFluidHandler) world.getTileEntity(x, y, z)).getFluidLightLevel();
        } else
            return 0;
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new UpgCtileentityFluidInfuser();
    }
}
