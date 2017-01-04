package com.nik7.upgcraft.reference;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class Render {

    public static class TankInternalDimension {

        public final static float TANK_Y_MIN = 0.05f;
        public final static float TANK_SIZE = 0.705f;
        public final static float TANK_Y_MAX = 0.86f;

        public final static float FM_Y_MIN = 0.05f;
        public final static float FM_SIZE = 0.62f;
        public final static float FM_Y_MAX = 0.35f;

    }
}