package com.klinbee.keenlib;

import com.klinbee.keenlib.registration.CommonRegistrations;
import com.klinbee.keenlib.registration.TypedCodec;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(KeenLibConstants.MOD_ID)
public class KeenLibForge {

    private static final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public KeenLibForge() {
        CommonRegistrations.registerCommon(KeenLibForge::register);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static <C> void register(Registry<C> registry, TypedCodec<C> typedCodec) {
        DeferredRegister<C> deferredRegister =
                DeferredRegister.create(
                        registry.key(),
                        KeenLibConstants.MOD_NAMESPACE
                );
        deferredRegister.register(typedCodec.type(), typedCodec::codec);
        deferredRegister.register(eventBus);
    }
}