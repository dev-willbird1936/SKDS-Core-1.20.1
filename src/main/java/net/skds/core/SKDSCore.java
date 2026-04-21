package net.skds.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.skds.core.network.PacketHandler;

@Mod(SKDSCore.MOD_ID)
public class SKDSCore {

    public static final String MOD_ID = "skds_core";
    public static final String MOD_NAME = "SKDS Core";
    public static final Logger LOGGER = LogManager.getLogger();

    public static Events EVENTS = new Events();

    private final ModContainer container;

    public SKDSCore(IEventBus modBus, ModContainer container) {
        this.container = container;

        modBus.addListener(this::setup);

        NeoForge.EVENT_BUS.register(EVENTS);

        SKDSCoreConfig.init(container);
        PacketHandler.init(modBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            SKDSCoreClientHooks.init(modBus, container);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}
