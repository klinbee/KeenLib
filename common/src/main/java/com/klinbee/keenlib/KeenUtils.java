package com.klinbee.keenlib;

import net.minecraft.resources.ResourceLocation;

public final class KeenUtils {

    private KeenUtils() {
    }

    public static ResourceLocation resLoc(String name) {
        return new ResourceLocation(KeenLibConstants.MOD_NAMESPACE, name);
    }
}
