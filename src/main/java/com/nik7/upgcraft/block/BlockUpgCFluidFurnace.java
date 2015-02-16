package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class BlockUpgCFluidFurnace extends BlockUpgC implements ITileEntityProvider {

    private final Random rand = new Random();

    public BlockUpgCFluidFurnace() {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_FURNACE);
        this.setBlockTextureName("cobblestone");
        setStepSound(soundTypePiston);
        setHardness(5.0F);

    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    public int getRenderType() {
        return RenderIds.FLUID_FURNACE;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(UpgradeCraft.instance, GUIs.FLUID_FURNACE.ordinal(), world, x, y, z);

        }
        return true;
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        UpgCtileentityFluidFurnace te = (UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z);
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
                    world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 5:
                    world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                    break;
                case 2:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                    break;
                case 3:
                    world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                    break;
            }
        }
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
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        UpgCtileentityFluidFurnace tile = ((UpgCtileentityFluidFurnace) world.getTileEntity(x, y, z));
        if (tile.fluidLevel > 0) {
            return FluidRegistry.getFluid("lava").getLuminosity();
        } else
            return 0;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCtileentityFluidFurnace();
    }
}
