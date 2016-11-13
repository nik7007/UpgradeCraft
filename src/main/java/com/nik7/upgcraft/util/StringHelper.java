package com.nik7.upgcraft.util;


public class StringHelper {
    public static String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
