package com.nik7.upgcraft.block;


import com.nik7.upgcraft.util.ItemHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockUpgCContainerOrientable extends BlockUpgC implements ITileEntityProvider {

    public BlockUpgCContainerOrientable(Material material) {
        super(material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        byte meta = 0;
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (l) {
            case 0:
                meta = 2;
                break;
            case 1:
                meta = 5;
                break;
            case 2:
                meta = 3;
                break;
            case 3:
                meta = 4;
                break;
        }

        world.setBlockMetadataWithNotify(x, y, z, meta, 3);

    }

    protected void dropItems(World world, int x, int y, int z, ItemStack itemStack, Random rand) {

        ItemHelper.dropItems(world, x, y, z, itemStack, rand);

    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
