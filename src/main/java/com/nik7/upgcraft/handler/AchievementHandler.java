package com.nik7.upgcraft.handler;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.LinkedList;
import java.util.List;

import static com.nik7.upgcraft.handler.AchievementHandler.AchievementList.*;
import static com.nik7.upgcraft.reference.Reference.MOD_ID;
import static com.nik7.upgcraft.reference.Reference.MOD_NAME;
import static net.minecraft.stats.AchievementList.bookcase;
import static net.minecraft.stats.AchievementList.mineWood;


public class AchievementHandler {

    private final static AchievementHandler ACHIEVEMENT_HANDLER = new AchievementHandler();

    private List<Achievement> achievementList;
    private AchievementPage achievementPage;

    /*public static AchievementHandler getInstance() {
        return ACHIEVEMENT_HANDLER;
    }*/

    public static void init() {
        ACHIEVEMENT_HANDLER.achievementList = new LinkedList<>();
        ACHIEVEMENT_HANDLER.addAchievement(GET_SLIME);
        ACHIEVEMENT_HANDLER.addAchievement(WOODEN_TANK);
        ACHIEVEMENT_HANDLER.addAchievement(FLUID_INFUSION);
        ACHIEVEMENT_HANDLER.addAchievement(CLAY_INGOT);
        ACHIEVEMENT_HANDLER.addAchievement(CLAY_TANK);
        ACHIEVEMENT_HANDLER.addAchievement(SLIME_OBSIDIAN);
        ACHIEVEMENT_HANDLER.addAchievement(ENDER_TANK);
        ACHIEVEMENT_HANDLER.achievementPage = new AchievementPage(MOD_NAME, ACHIEVEMENT_HANDLER.achievementList.toArray(new Achievement[ACHIEVEMENT_HANDLER.achievementList.size()]));
        AchievementPage.registerAchievementPage(ACHIEVEMENT_HANDLER.achievementPage);
    }

    private AchievementHandler() {

    }

    private void addAchievement(Achievement achievement) {
        if (!achievementList.contains(achievement))
            this.achievementList.add(achievement);
    }

    public static void pickupAchievement(EntityPlayer player, ItemStack itemStack) {

        if (player != null && itemStack != null) {
            Item item = itemStack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block == ModBlocks.blockUpgCClayFluidTank) {
                    if (item.getDamage(itemStack) > 1)
                        player.addStat(CLAY_TANK);
                }

            } else if (item == Items.slime_ball)
                player.addStat(GET_SLIME);

        }

    }

    public static void craftAchievement(EntityPlayer player, ItemStack itemStack) {

        if (player != null && itemStack != null) {
            Item item = itemStack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block == ModBlocks.blockUpgCWoodenFluidTank)
                    player.addStat(WOODEN_TANK);
                if (block == ModBlocks.blockUpgCFluidInfuser)
                    player.addStat(FLUID_INFUSION);
                if (block == ModBlocks.blockUpgCSlimyObsidian)
                    player.addStat(SLIME_OBSIDIAN);
                if (block == ModBlocks.blockUpgCEnderFluidTank)
                    player.addStat(ENDER_TANK);
            } else {
                if (item == ModItems.itemUpgCClayIngot)
                    player.addStat(CLAY_INGOT);
            }
        }

    }

    public static class AchievementList {

        public static final Achievement GET_SLIME = createAchievement("get.slime", 0, 0, new ItemStack(ModBlocks.blockUpgCSlimyLog), mineWood, false, false);
        public static final Achievement WOODEN_TANK = createAchievement("wooden.tank", -2, 0, new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 0), GET_SLIME, false, false);
        public static final Achievement FLUID_INFUSION = createAchievement("fluid.infusion", 0, -2, new ItemStack(ModBlocks.blockUpgCFluidInfuser), WOODEN_TANK, false, true);
        public static final Achievement CLAY_INGOT = createAchievement("clay.ingot", 0, -4, new ItemStack(ModItems.itemUpgCClayIngot), FLUID_INFUSION, false, false);
        public static final Achievement CLAY_TANK = createAchievement("clay.tank", 0, -7, new ItemStack(ModBlocks.blockUpgCClayFluidTank, 1, 3), CLAY_INGOT, false, true);
        public static final Achievement SLIME_OBSIDIAN = createAchievement("slime.obsidian", 2, -2, new ItemStack(ModBlocks.blockUpgCSlimyObsidian), FLUID_INFUSION, false, false);
        public static final Achievement ENDER_TANK = createAchievement("ender.tank", 3, -3, new ItemStack(ModBlocks.blockUpgCEnderFluidTank), SLIME_OBSIDIAN, false, true);


        private static Achievement createAchievement(String achievementName, int column, int row, ItemStack itemIn, Achievement parent, boolean isIndependent, boolean isSpecial) {

            Achievement achievement = new Achievement("achievement." + MOD_ID + "." + achievementName, MOD_ID + ":" + achievementName, column, row, itemIn, parent);

            if (isIndependent)
                achievement.initIndependentStat();
            if (isSpecial)
                achievement.setSpecial();

            achievement.registerStat();

            return achievement;

        }

    }
}
