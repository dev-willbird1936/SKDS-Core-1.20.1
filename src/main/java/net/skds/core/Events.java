package net.skds.core;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.skds.core.api.IWorldExtended;
import net.skds.core.util.blockupdate.BlockUpdataer;
import net.skds.core.util.blockupdate.WWSGlobal;

public class Events {

    private static long inTickTime = System.nanoTime();
    private static int lastTickTime = 0;

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload e) {

        if (!(e.getLevel() instanceof Level w)) {
            return;
        }
        WWSGlobal wwsg = IWorldExtended.getWWS(w);
        if (wwsg != null) {
            wwsg.unloadWorld(w);
        }
        IWorldExtended.removeWWS(w);
        if (!w.isClientSide && w instanceof ServerLevel serverLevel) {
            BlockUpdataer.onWorldUnload(serverLevel);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load e) {

        if (!(e.getLevel() instanceof Level w)) {
            return;
        }
        IWorldExtended.addWWS(w);
       // WWSGlobal.loadWorld(w);
        if (!w.isClientSide && w instanceof ServerLevel serverLevel) {
            BlockUpdataer.onWorldLoad(serverLevel);
        }
    }

    @SubscribeEvent
    public void tick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        WWSGlobal wwsg = IWorldExtended.getWWS(level);
        if (wwsg == null) {
            return;
        }
        wwsg.tickIn();
    }

    @SubscribeEvent
    public void tick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        WWSGlobal wwsg = IWorldExtended.getWWS(level);
        if (wwsg == null) {
            return;
        }
        wwsg.tickOut();
        lastTickTime = (int) (System.nanoTime() - inTickTime);
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Pre event) {
        inTickTime = System.nanoTime();
        BlockUpdataer.tick(true);
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {
        BlockUpdataer.tick(false);
        lastTickTime = (int) (System.nanoTime() - inTickTime);
    }

    public static int getLastTickTime() {
        return lastTickTime;
    }

    public static int getRemainingTickTimeNanos() {
        return 50_000_000 - (int) (System.nanoTime() - inTickTime);
    }

    public static int getRemainingTickTimeMicros() {
        return getRemainingTickTimeNanos() / 1000;
    }

    public static int getRemainingTickTimeMilis() {
        return getRemainingTickTimeNanos() / 1000_000;
    }
}
