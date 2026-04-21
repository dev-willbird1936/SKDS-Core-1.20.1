package net.skds.core;

import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.skds.core.config.Main;
import net.skds.core.config.PerformancePreset;

public class SKDSCoreConfig {
    private static final int DEFAULT_TIMEOUT = 4;
    private static final int DEFAULT_MIN_BLOCK_UPDATES = 500;
    private static final int VERY_LOW_TIMEOUT = 1;
    private static final int VERY_LOW_MIN_BLOCK_UPDATES = 125;
    private static final int LOW_TIMEOUT = 2;
    private static final int LOW_MIN_BLOCK_UPDATES = 250;
    private static final int HIGH_TIMEOUT = 6;
    private static final int HIGH_MIN_BLOCK_UPDATES = 1_000;
    private static final int VERY_HIGH_TIMEOUT = 8;
    private static final int VERY_HIGH_MIN_BLOCK_UPDATES = 2_000;

    public static final Main COMMON;
    //public static final Waterlogged WATERLOGGED;
    private static final ModConfigSpec SPEC;//, SPEC_WL;


    public static final int MAX_FLUID_LEVEL = 8;

    static {
        Pair<Main, ModConfigSpec> cm = new ModConfigSpec.Builder().configure(Main::new);
        COMMON = cm.getLeft();
        SPEC = cm.getRight();

        //Pair<Waterlogged, ForgeConfigSpec> wl = new ForgeConfigSpec.Builder().configure(Waterlogged::new);
        ///WATERLOGGED = wl.getLeft();
        //SPEC_WL = wl.getRight();

        // FINITE_WATER = COMMON.finiteWater.get();
        // MAX_EQ_DIST = COMMON.maxEqDist.get();
    }

    public static void init(ModContainer container) {
        Paths.get(System.getProperty("user.dir"), "config", SKDSCore.MOD_ID).toFile().mkdir();
        container.registerConfig(ModConfig.Type.COMMON, SPEC, Paths.get(SKDSCore.MOD_ID, "main.toml").toString());
        //ModLoadingContext.get().registerConfig(Type.COMMON, SPEC_WL, PhysEX.MOD_ID + "/waterlogged.toml");
    }

    public static PerformancePreset getPerformancePreset() {
        return COMMON.performancePreset.get();
    }

    public static int getManualTimeoutCutoff() {
        return COMMON.timeoutCutoff.get();
    }

    public static int getManualMinBlockUpdates() {
        return COMMON.minBlockUpdates.get();
    }

    public static int getTimeoutCutoff() {
        return switch (getPerformancePreset()) {
            case VERY_LOW -> VERY_LOW_TIMEOUT;
            case LOW -> LOW_TIMEOUT;
            case DEFAULT -> DEFAULT_TIMEOUT;
            case HIGH -> HIGH_TIMEOUT;
            case VERY_HIGH -> VERY_HIGH_TIMEOUT;
            case CUSTOM -> COMMON.timeoutCutoff.get();
        };
    }

    public static int getMinBlockUpdates() {
        return switch (getPerformancePreset()) {
            case VERY_LOW -> VERY_LOW_MIN_BLOCK_UPDATES;
            case LOW -> LOW_MIN_BLOCK_UPDATES;
            case DEFAULT -> DEFAULT_MIN_BLOCK_UPDATES;
            case HIGH -> HIGH_MIN_BLOCK_UPDATES;
            case VERY_HIGH -> VERY_HIGH_MIN_BLOCK_UPDATES;
            case CUSTOM -> COMMON.minBlockUpdates.get();
        };
    }

    public static void setPerformancePreset(PerformancePreset preset) {
        COMMON.performancePreset.set(preset);
    }

    public static void setManualTimeoutCutoff(int value) {
        COMMON.timeoutCutoff.set(value);
    }

    public static void setManualMinBlockUpdates(int value) {
        COMMON.minBlockUpdates.set(value);
    }

    public static void save() {
        SPEC.save();
    }
}
