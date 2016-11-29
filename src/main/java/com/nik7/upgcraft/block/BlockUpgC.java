package com.nik7.upgcraft.block;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public abstract class BlockUpgC extends Block {

    public BlockUpgC(Material blockMaterial, String blockName) {
        super(blockMaterial);
        setUnlocalizedName(Reference.RESOURCE_PREFIX + blockName);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
        this.setRegistryName(blockName);

    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticles(World worldIn, BlockPos pos, Random rand, EnumParticleTypes... particles) {

        float f = (float) pos.getX() + 0.5F;
        float f1 = (float) pos.getY() + rand.nextFloat();
        float f2 = (float) pos.getZ() + 0.5F;
        float f3 = 0.52F;
        float f4 = rand.nextFloat() * 0.6F - 0.3F;

        for (EnumParticleTypes p : particles) {

            worldIn.spawnParticle(p, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(p, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(p, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(p, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);

            worldIn.spawnParticle(p, pos.getX() + 0.5D - rand.nextDouble(), (double) pos.getY() + 1, pos.getZ() + 0.5D - rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }


    }

}
