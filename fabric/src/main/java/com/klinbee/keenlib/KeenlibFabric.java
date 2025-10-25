package com.klinbee.keenlib;

import net.fabricmc.api.ModInitializer;

public class KeenlibFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        KeenlibConstants.LOG.info("Hello Fabric world!");
    }
}
