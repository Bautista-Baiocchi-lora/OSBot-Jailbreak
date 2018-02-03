package org.osbot.jailbreak.threadding;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ethan on 2/3/2018.
 */
public class ThreadExecutor {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static void stop() {
        threadPoolExecutor.shutdown();
        threadPoolExecutor.purge();
        threadPoolExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    public static void startThread(final Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
