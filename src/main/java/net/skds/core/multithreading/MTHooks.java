package net.skds.core.multithreading;

import java.util.function.Function;

import net.neoforged.neoforge.common.NeoForge;
import net.skds.core.SKDSCoreConfig;
import net.skds.core.api.multithreading.ITaskRunnable;
import net.skds.core.events.SyncTasksHookEvent;

public class MTHooks {
	public static int COUNTS = 0;
	public static int TIME = 0;

	// Set by a downstream mod (e.g. WPO's WorldWorkSet::nextTask) at mod construction.
	// MTHooks itself has no task queue of its own; it only owns the worker fork/join.
	private static volatile Function<Integer, ITaskRunnable> taskSource = null;

	public static void registerTaskSource(Function<Integer, ITaskRunnable> source) {
		taskSource = source;
	}

	public static void afterWorldsTick() {
		TIME = SKDSCoreConfig.getTimeoutCutoff();
		COUNTS = SKDSCoreConfig.getMinBlockUpdates();

		Function<Integer, ITaskRunnable> source = taskSource;
		if (source != null && SKDSCoreConfig.isMultithreadedFluidTickingEnabled()) {
			ThreadProvider.doSyncFork(source);
			try {
				ThreadProvider.waitForStop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// Single-threaded fallback: drain synchronously on this (main) thread instead.
			// Never run both in the same tick - the single-threaded drain does not go
			// through TaskBlocker, so it must not overlap with forked worker tasks.
			NeoForge.EVENT_BUS.post(new SyncTasksHookEvent());
		}
	}
}
