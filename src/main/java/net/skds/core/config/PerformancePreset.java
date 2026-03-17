package net.skds.core.config;

import net.minecraft.network.chat.Component;

public enum PerformancePreset {
    VERY_LOW,
    LOW,
    DEFAULT,
    HIGH,
    VERY_HIGH,
    CUSTOM;

    public Component getDisplayName() {
        return Component.translatable("skds_core.config.performancePreset." + name().toLowerCase());
    }
}
