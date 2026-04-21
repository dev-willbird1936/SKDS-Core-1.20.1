package net.skds.core;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.skds.core.config.SKDSCoreConfigScreen;

final class SKDSCoreClientHooks {

    private SKDSCoreClientHooks() {
    }

    static void init(IEventBus modBus, ModContainer container) {
        modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (modContainer, modListScreen) -> new SKDSCoreConfigScreen(modListScreen)
        )));
    }
}
