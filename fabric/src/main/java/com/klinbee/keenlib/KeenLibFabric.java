package com.klinbee.keenlib;

import com.klinbee.keenlib.defs.TypedCodec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class KeenLibFabric implements ModInitializer {

    public static <C> void register(Registry<C> registry, TypedCodec<C> typedCodec) {
        ResourceLocation resourceLocation = KeenUtils.resLoc(typedCodec.type());
        ResourceKey<C> resourceKey = ResourceKey.create(registry.key(), resourceLocation);
        Registry.register(registry, resourceKey, typedCodec.codec());
    }

    @Override
    public void onInitialize() {
        KeenLibRegistry.registerCommon(KeenLibFabric::register);
    }
}
