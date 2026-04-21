package net.skds.core.config;

import java.util.function.Function;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.skds.core.SKDSCore;

public class Main {

    public final ModConfigSpec.EnumValue<PerformancePreset> performancePreset;
    public final ModConfigSpec.IntValue minBlockUpdates, timeoutCutoff;

    // public final ForgeConfigSpec.ConfigValue<ArrayList<String>> ss;
    // private final ForgeConfigSpec.IntValue maxFluidLevel;

    public Main(ModConfigSpec.Builder innerBuilder) {
        Function<String, ModConfigSpec.Builder> builder = name -> innerBuilder.translation(SKDSCore.MOD_ID + ".config." + name);

        innerBuilder.push("Performance");

        performancePreset = builder.apply("performancePreset")
                .comment("DEFAULT uses timeout=4 and minBlockUpdates=500. VERY_LOW=1/125, LOW=2/250, HIGH=6/1000, VERY_HIGH=8/2000. Set to CUSTOM to use the manual values below.")
                .defineEnum("performancePreset", PerformancePreset.DEFAULT);
        minBlockUpdates = builder.apply("minBlockUpdates").comment("Manual value used only when performancePreset=CUSTOM. Minimal block updates per tick.")
                .defineInRange("minBlockUpdates", 500, 0, 1_000_000);
        timeoutCutoff = builder.apply("timeout")
                .comment("Manual value used only when performancePreset=CUSTOM. Time before tick end to stop synchronized tasks (ms).")
                .defineInRange("timeout", 4, 0, 50);

        innerBuilder.pop();
    }
}
