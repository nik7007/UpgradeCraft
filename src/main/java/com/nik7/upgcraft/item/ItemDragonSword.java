package com.nik7.upgcraft.item;

import com.google.common.collect.Multimap;
import com.nik7.upgcraft.init.ModItems;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemDragonSword extends ItemConcentratedEnderPearl {


    public ItemDragonSword() {
        super();
        this.setUnlocalizedName(Names.Items.DRAGON_SWORD);
        this.setTextureName(Texture.Items.DRAGON_SWORD);
        this.setFull3D();
    }


    @Override
    public float func_150893_a(ItemStack itemStack, Block block) {
        if (block == Blocks.web) {
            return 15.0F;
        } else {
            Material material = block.getMaterial();
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean isItemTool(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean reduceEnderPower(int amount, ItemStack itemStack, EntityPlayer player) {


        if (player.capabilities.isCreativeMode) {
            return super.reduceEnderPower(amount, itemStack, player);
        }

        if (getEnderPower(itemStack) <= 0) {
            return false;
        }

        ItemStack stack;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            stack = player.inventory.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == ModItems.itemConcentratedEnderPearl) {
                    if (getEnderPower(stack) > 0) {
                        return ((ItemConcentratedEnderPearl) stack.getItem()).reduceEnderPower(amount, stack, player);
                    }
                }
            }
        }


        return super.reduceEnderPower(amount, itemStack, player);

    }


    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingBase, EntityLivingBase player) {

        if (player instanceof EntityPlayer) {
            EntityPlayer ePlayer = (EntityPlayer) player;
            if (getEnderPower(itemStack) != 0) {
                reduceEnderPower(1, itemStack, ePlayer);
            }
        }

        return true;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        float maxAttack = 10;
        float attack = 10;

        if (stack != null) {
            attack += (int) (maxAttack * ((float) getEnderPower(stack) / (float) getMaxEnderPower()));
        }

        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double) (attack), 0));
        return multimap;
    }
}
