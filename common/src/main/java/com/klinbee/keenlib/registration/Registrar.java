package com.klinbee.keenlib.registration;

import net.minecraft.core.Registry;

@FunctionalInterface
public interface Registrar {
    <C> void register(Registry<C> registry, TypedCodec<C> typedCodec);
}