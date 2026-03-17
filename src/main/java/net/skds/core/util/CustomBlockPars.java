package net.skds.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

import net.minecraft.world.level.block.Block;
import net.skds.core.api.ICustomBlockPars;

public class CustomBlockPars implements ICustomBlockPars {

	private static final Map<Block, CustomBlockPars> BLOCK_PARS = new ConcurrentHashMap<>();
	private final Map<Class<?>, Object> pars = new HashMap<>();

	public static CustomBlockPars get(Block block) {
		return BLOCK_PARS.computeIfAbsent(block, unused -> new CustomBlockPars());
	}

	public static void set(Block block, CustomBlockPars pars) {
		BLOCK_PARS.put(block, pars);
	}

	@Override
	public void put(Class<?> key, Object o) {
		pars.put(key, o);
	}

	@Override
	public Object get(Class<?> key) {
		return pars.get(key);
	}
}
