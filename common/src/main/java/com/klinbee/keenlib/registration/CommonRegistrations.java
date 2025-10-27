package com.klinbee.keenlib.registration;

import com.klinbee.keenlib.densityfunctions.ArcCosine;
import com.klinbee.keenlib.placementmodifiers.CountOnEveryLayerPlacement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class CommonRegistrations {

    private CommonRegistrations() {
    }

    public static void registerCommon(Registrar registrar) {
        /// DensityFunctions
        registrar.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ArcCosine.TYPED_CODEC);

        /// PlacementModifiers
        registrar.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, CountOnEveryLayerPlacement.TYPED_CODEC);
    }
}