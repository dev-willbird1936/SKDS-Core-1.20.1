package net.skds.core.multithreading;

import java.util.function.Function;

import net.skds.core.api.multithreading.ITaskRunnable;

public class ThreadProvider {

	// Capped at MAX_WORKERS: TaskBlocker's conflict check is a single global synchronized
	// scan over all workers, and WorldWorkSet.nextTask only ever hands out indices below
	// this bound. More processors than this would just spin up idle threads.
	public static final int MAX_WORKERS = 4;
	public static int PROCESSORS = Math.min(MAX_WORKERS, Runtime.getRuntime().availableProcessors());
	public static UniversalWorkerThread[] THREADS = UniversalWorkerThread.create(PROCESSORS);

	
	public static void doSyncFork(Function<Integer, ITaskRunnable> sup) {

		for (UniversalWorkerThread t : THREADS) {
			t.forkSync(sup);
		}
	}

	public static void waitForStop() throws InterruptedException {
		for (UniversalWorkerThread t : THREADS) {
			t.waitForJoin();
		}
	}
}