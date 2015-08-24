package com.nik7.upgcraft.reference;

public class Names {

    public final static String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    public class Blocks {

        public final static String WOODEN_LIQUID_TANK = "WoodenTank";
        public final static String CLAY_LIQUID_TANK = "ClayTank";
        public final static String ENDER_TANK = "EnderTank";
        public final static String SLIMY_LOG = "SlimyLog";
        public final static String SLIMY_OBSIDIAN = "SlimyObsidian";
        public final static String FLUID_HOPPER = "FluidHopper";
        public final static String ENDER_HOPPER = "EnderHopper";
        public final static String FLUID_FURNACE = "FluidFurnace";
        public final static String FLUID_INFUSE = "FluidInfuser";
        public final static String TERMO_FLUID_FURNACE = "Termo" + FLUID_FURNACE;
        public final static String ACTIVE_MAKER = "ActiveMaker";
    }

    public class Items {

        public final static String DRAGON_SWORD = "DragonSword";
        public final static String CONCENTRATED_ENDER_PEARL = "ConcentratedEnderPearl";
        public final static String CLAY_IRON_INGOT = "ClayIronIngot";
        public final static String PERSONAL_INFORMATION = "PersonalInformation";
        public final static String ACTIVE_LAVA_BUCKET = "ActiveLavaBucket";

    }

    public class Fluid {

        public final static String ACTIVE_LAVE = "ActiveLava";

    }

    public class TileEntity {
        public final static String UPGC_TILE_ENTITY_TANK = Reference.MOD_ID + ":" + "LiquidTankEntity";
        public final static String UPGC_TILE_ENTITY_FLUID_HOPPER = Reference.MOD_ID + ":" + "FluidHopperEntity";
        public final static String UPGC_TILE_ENTITY_ENDER_HOPPER = Reference.MOD_ID + ":" + "EnderHopperEntity";
        public final static String UPGC_TILE_ENTITY_FLUID_FURNACE = Reference.MOD_ID + ":" + "FluidFurnaceEntity";
        public final static String UPG_TILE_ENTITY_FLUID_INFUSER = Reference.MOD_ID + ":" + "FluidInfuserEntity";
        public final static String UPG_TILE_ENTITY_ACTIVE_MAKER = Reference.MOD_ID + ":" + "ActiveMaker";

    }

    public class Inventory {
        public final static String UPGC_FLUID_FURNACE = Reference.MOD_ID + ":" + "container.fluidfurnace";
        public final static String UPGC_FLUID_INFUSER = Reference.MOD_ID + ":" + "container.fluidinfuser";
        public final static String UPGC_ENDER_HOPPER = Reference.MOD_ID + ":" + "container.enderhopper";
        public final static String UPGC_TERMO_FURNACE = Reference.MOD_ID + ":" + "container.termofurnace";
        public final static String UPGC_ACTIVE_MAKER = Reference.MOD_ID + ":" + "container.activemaker";
    }


}

