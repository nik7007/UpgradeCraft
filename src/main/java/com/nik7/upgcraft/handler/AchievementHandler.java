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
import static net.minecraft.stats.AchievementList.mineWood;


public class AchievementHandler {

    private final static AchievementHandler ACHIEVEMENT_HANDLER = new AchievementHandler();

    private List<Achievement> achievementList;
    private AchievementPage achievementPage;

    /*public static AchievementHandler getInstance() {
        return ACHIEVEMENT_HANDLER;
    }*/

    public static void init() {

    }

    private AchievementHandler() {
        this.achievementList = new LinkedList<>();
        this.addAchievement(GET_SLIME);
        this.addAchievement(WOODEN_TANK);
        this.addAchievement(FLUID_INFUSION);
        this.addAchievement(CLAY_TANK);
        this.addAchievement(SLIME_OBSIDIAN);
        this.addAchievement(ENDER_TANK);
        this.achievementPage = new AchievementPage(MOD_NAME, achievementList.toArray(new Achievement[achievementList.size()]));
        AchievementPage.registerAchievementPage(achievementPage);
    }

    private void addAchievement(Achievement achievement) {
        if (!achievementList.contains(achievement))
            this.achievementList.add(achievement);
    }

    public static void pickupAchievement(EntityPlayer player, ItemStack itemStack) {

        if (player != null && itemStack != null) {
            Item item = itemStack.getItem();
            if (item == Items.slime_ball)
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
                    player.addStat(CLAY_TANK);
            }
        }

    }

    public static class AchievementList {

        public static final Achievement GET_SLIME = createAchievement("get.slime", 0, 0, new ItemStack(ModBlocks.blockUpgCSlimyLog), mineWood, false, false);
        public static final Achievement WOODEN_TANK = createAchievement("wooden.tank", -2, 0, new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 0), GET_SLIME, false, false);
        public static final Achievement FLUID_INFUSION = createAchievement("fluid.infusion", 0, -2, new ItemStack(ModBlocks.blockUpgCFluidInfuser), WOODEN_TANK, false, true);
        public static final Achievement CLAY_TANK = createAchievement("clay.tank", 0, -4, new ItemStack(ModBlocks.blockUpgCClayFluidTank, 1, 1), FLUID_INFUSION, false, false);
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
