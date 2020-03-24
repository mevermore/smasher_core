package com.smasher.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author matao
 * @date 2019/5/30
 */
public class IExecutor extends ThreadPoolExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(3, CPU_COUNT / 2);
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
    private static final long KEEP_ALIVE_TIME = 0L;


    private static IExecutor INSTANCE = null;


    public static IExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IExecutor(CORE_POOL_SIZE,
                    MAX_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        return INSTANCE;
    }


    private IExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                      TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }


    public void executeRunnable(Runnable runnable) {
        execute(runnable);
    }

    public <T> Future<T> submitFuture(Callable<T> callable) {
        return submit(callable);
    }

}
