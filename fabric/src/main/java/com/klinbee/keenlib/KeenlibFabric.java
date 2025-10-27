package com.klinbee.keenlib;

import com.klinbee.keenlib.registration.CommonRegistrations;
import com.klinbee.keenlib.registration.TypedCodec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class KeenlibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonRegistrations.registerCommon(KeenlibFabric::register);
    }

    public static <C> void register(Registry<C> registry, TypedCodec<C> typedCodec) {
        ResourceLocation resourceLocation = new ResourceLocation(KeenlibConstants.MOD_NAMESPACE, typedCodec.type());
        ResourceKey<C> resourceKey = ResourceKey.create(registry.key(), resourceLocation);
        Registry.register(registry, resourceKey, typedCodec.codec());
    }
}
