package net.skds.core.multithreading;

import net.neoforged.neoforge.common.NeoForge;
import net.skds.core.SKDSCoreConfig;
import net.skds.core.events.SyncTasksHookEvent;

public class MTHooks {
	public static int COUNTS = 0;
	public static int TIME = 0;

	public static void afterWorldsTick() {
		TIME = SKDSCoreConfig.getTimeoutCutoff();
		COUNTS = SKDSCoreConfig.getMinBlockUpdates();

		NeoForge.EVENT_BUS.post(new SyncTasksHookEvent());

		///ThreadProvider.doSyncFork(WWSGlobal::nextTask);
		try {
			ThreadProvider.waitForStop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
