package net.skds.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.skds.core.config.SKDSCoreConfigScreen;
import net.skds.core.network.PacketHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("skds_core")
public class SKDSCore
{

    public static final String MOD_ID = "skds_core";
    public static final String MOD_NAME = "SKDS Core";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static Events EVENTS = new Events();

    public SKDSCore() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(EVENTS);
        MinecraftForge.EVENT_BUS.register(this);

        SKDSCoreConfig.init();
        PacketHandler.init();
    }
    

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ModList.get().getModContainerById(MOD_ID).ifPresent(container ->
                container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                        () -> new ConfigScreenHandler.ConfigScreenFactory(SKDSCoreConfigScreen::new))));
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {        
    }

    private void processIMC(final InterModProcessEvent event) {
    }
}
