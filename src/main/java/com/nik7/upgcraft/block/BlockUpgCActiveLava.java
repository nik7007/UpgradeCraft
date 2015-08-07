package com.nik7.upgcraft.block;


import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.StringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlockUpgCActiveLava extends BlockFluidClassic {

    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;

    public BlockUpgCActiveLava() {
        super(ModFluids.ActiveLava, Material.lava);
        ModFluids.ActiveLava.setBlock(this);
        this.setBlockName(Names.Fluid.ACTIVE_LAVE);
        this.setBlockTextureName(Texture.Fluid.ACTIVE_LAVE + "_still");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? stillIcon : flowingIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(Texture.Fluid.ACTIVE_LAVE + "_still");
        flowingIcon = register.registerIcon(Texture.Fluid.ACTIVE_LAVE + "_flow");
        ModFluids.ActiveLava.setStillIcon(stillIcon);
        ModFluids.ActiveLava.setFlowingIcon(flowingIcon);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Names.RESOURCE_PREFIX, StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        worldAction(world, x, y, z);
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        worldAction(world, x, y, z);
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    private void worldAction(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) == this) {
            if (this.blockMaterial == Material.lava) {
                boolean touchWater = false;

                if (world.getBlock(x, y, z - 1).getMaterial() == Material.water) {
                    touchWater = true;
                }

                if (touchWater || world.getBlock(x, y, z + 1).getMaterial() == Material.water) {
                    touchWater = true;
                }

                if (touchWater || world.getBlock(x - 1, y, z).getMaterial() == Material.water) {
                    touchWater = true;
                }

                if (touchWater || world.getBlock(x + 1, y, z).getMaterial() == Material.water) {
                    touchWater = true;
                }

                if (touchWater || world.getBlock(x, y + 1, z).getMaterial() == Material.water) {
                    touchWater = true;
                }

                if (touchWater) {
                    int metadata = world.getBlockMetadata(x, y, z);

                    if (metadata == 0) {
                        world.setBlock(x, y, z, Blocks.obsidian);
                    } else if (metadata <= 4) {
                        world.setBlock(x, y, z, Blocks.cobblestone);
                    }

                    this.fluidEffects(world, x, y, z);
                }
            }
        }
    }


    protected void fluidEffects(World world, int x, int y, int z) {
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l) {
            world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + 1.2D, (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        toStone(world, x, y, z);
        toFire(world, x, y, z, random);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.setFire(20);
        entity.attackEntityFrom(DamageSource.lava, 2.0F);
        entity.attackEntityFrom(DamageSource.onFire, 1.0F);
    }

    private void toFire(World world, int x, int y, int z, Random random) {

        int rnd = random.nextInt(3);
        int i;

        for (i = 0; i < rnd; ++i) {
            x += random.nextInt(3) - 1;
            ++y;
            z += random.nextInt(3) - 1;
            Block block = world.getBlock(x, y, z);
            if (block.getMaterial() == Material.air) {
                if (this.isFlammable(world, x - 1, y, z) || this.isFlammable(world, x + 1, y, z) || this.isFlammable(world, x, y, z - 1) || this.isFlammable(world, x, y, z + 1) || this.isFlammable(world, x, y - 1, z) || this.isFlammable(world, x, y + 1, z)) {
                    world.setBlock(x, y, z, Blocks.fire);
                    return;
                }
            }
        }

        if (rnd == 0) {
            i = x;
            int rndZ = z;

            for (int j = 0; j < 3; ++j) {
                x = i + random.nextInt(3) - 1;
                z = rndZ + random.nextInt(3) - 1;

                if (world.isAirBlock(x, y + 1, z) && this.isFlammable(world, x, y, z)) {
                    world.setBlock(x, y + 1, z, Blocks.fire);
                }
            }
        }
    }

    private void toStone(World world, int x, int y, int z) {

        if (this.checkForStone(world, x, y - 1, z)) {
            if (this.blockMaterial == Material.lava && world.getBlock(x, y - 1, z).getMaterial() == Material.water) {
                world.setBlock(x, y - 1, z, Blocks.stone);
                this.fluidEffects(world, x, y - 1, z);
            }
        }
    }

    private boolean checkForStone(World world, int x, int y, int z) {
        Material material = world.getBlock(x, y, z).getMaterial();
        return material != this.blockMaterial && (material != Material.lava && !this.checkBlock(world, x, y, z));
    }

    private boolean checkBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return !(block != Blocks.wooden_door && block != Blocks.iron_door && block != Blocks.standing_sign && block != Blocks.ladder && block != Blocks.reeds) || (block.getMaterial() == Material.portal || block.getMaterial().blocksMovement());
    }

    private boolean isFlammable(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).getMaterial().getCanBurn();
    }
}
