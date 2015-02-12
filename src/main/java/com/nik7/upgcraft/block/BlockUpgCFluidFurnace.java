package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockUpgCFluidFurnace extends BlockUpgC implements ITileEntityProvider {

    private final Random rand = new Random();

    public BlockUpgCFluidFurnace() {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_FURNACE);

    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int n) {

        UpgCtileentityFluidFurnace te = (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z);

        ItemStack itemStackIn = te.getStackInSlot(UpgCtileentityFluidFurnace.INPUT);
        ItemStack itemStackOut = te.getStackInSlot(UpgCtileentityFluidFurnace.OUTPUT);

        dropItems(world, x, y, z, itemStackIn);
        dropItems(world, x, y, z, itemStackOut);

        super.breakBlock(world, x, y, z, block, n);

    }

    private void dropItems(World world, int x, int y, int z, ItemStack itemStack) {

        if (itemStack != null) {
            float dX = this.rand.nextFloat() * 0.8F + 0.1F;
            float dY = this.rand.nextFloat() * 0.8F + 0.1F;
            float dZ = this.rand.nextFloat() * 0.8F + 0.1F;

            while (itemStack.stackSize > 0) {
                int j1 = this.rand.nextInt(21) + 10;

                if (j1 > itemStack.stackSize) {
                    j1 = itemStack.stackSize;
                }

                itemStack.stackSize -= j1;
                EntityItem entityitem = new EntityItem(world, (double) ((float) x + dX), (double) ((float) y + dY), (double) ((float) z + dZ), new ItemStack(itemStack.getItem(), j1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                world.spawnEntityInWorld(entityitem);
            }
        }

    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityFluidFurnace();
    }
}
