package com.nik7.upgcraft.reference;

public class Names {

    public final static String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    public class Blocks {

        public final static String WOODEN_LIQUID_TANK = "WoodenTank";
        public final static String SLIMY_LOG = "SlimyLog";
        public final static String FLUID_HOPPER = "FluidHopper";
        public final static String FLUID_FURNACE = "FluidFurnace";
    }

    public class Items {

        public final static String DRAGON_SWORD = "DragonSword";
        public final static String CONCENTRATED_ENDER_PEARL = "ConcentratedEnderPearl";

    }

    public class TileEntity {
        public final static String UPGC_TILE_ENTITY_TANK = Reference.MOD_ID + ":" + "LiquidTankEntity";
        public final static String UPGC_TILE_ENTITY_FLUID_HOPPER = Reference.MOD_ID + ":" + "FluidHopperEntity";
        public final static String UPGC_TILE_FLUID_FURNACE = Reference.MOD_ID + ":" + "FluidFurnaceEntity";

    }

    public class Inventory {
        public final static String UPGC_FLUID_FURNACE =  Reference.MOD_ID + ":" +"container.fluidfurnace";
    }


}

