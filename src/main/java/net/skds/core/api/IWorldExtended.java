package net.skds.core.api;

import net.minecraft.world.level.Level;
import net.skds.core.util.blockupdate.WWSGlobal;

public interface IWorldExtended {
	java.util.Map<Level, WWSGlobal> WORLD_WORKSETS = new java.util.concurrent.ConcurrentHashMap<>();

	static WWSGlobal getWWS(Level level) {
		return WORLD_WORKSETS.get(level);
	}

	static WWSGlobal addWWS(Level level) {
		return WORLD_WORKSETS.computeIfAbsent(level, key -> new WWSGlobal(level));
	}

	static void removeWWS(Level level) {
		WORLD_WORKSETS.remove(level);
	}
}
