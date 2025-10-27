package com.klinbee.keenlib;

import com.klinbee.keenlib.registration.CommonRegistrations;
import com.klinbee.keenlib.registration.TypedCodec;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(KeenlibConstants.MOD_ID)
public class KeenlibForge {

    private static final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public KeenlibForge() {
        CommonRegistrations.registerCommon(KeenlibForge::register);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static <C> void register(Registry<C> registry, TypedCodec<C> typedCodec) {
        DeferredRegister<C> deferredRegister =
                DeferredRegister.create(
                        registry.key(),
                        KeenlibConstants.MOD_NAMESPACE
                );
        deferredRegister.register(typedCodec.type(), typedCodec::codec);
        deferredRegister.register(eventBus);
    }
}