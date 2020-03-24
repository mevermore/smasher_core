package com.smasher.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author matao
 */
public class ThreadPool {
    private static Object lockobj = new Object();

    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 2;
    public static final int PRIORITY_DOWN = 3;
    public static final int PRIORITY_WRITE_LOG = 4;
    public static final int PRIORITY_GAME_DOWNLOAD = 5;

    private static ExecutorService mHighPool;
    private static ExecutorService mMediumPool;
    private static ExecutorService mLowPool;
    private static ExecutorService mDownloadPool;
    private static ExecutorService mLogWritePool;
    private static ExecutorService mGameDownLoadPool;

    private static ExecutorService getMediumInstance() {
        synchronized (lockobj) {
            if (mMediumPool == null || mMediumPool.isShutdown()) {
                mMediumPool = Executors.newFixedThreadPool(3);
            }
            return mMediumPool;
        }
    }

    private static ExecutorService getHighInstance() {
        synchronized (lockobj) {
            if (mHighPool == null || mHighPool.isShutdown()) {
                mHighPool = Executors.newCachedThreadPool();
            }
            return mHighPool;
        }
    }

    private static ExecutorService getLowInstance() {
        synchronized (lockobj) {
            if (mLowPool == null || mLowPool.isShutdown()) {
                mLowPool = Executors.newSingleThreadExecutor();
            }
            return mLowPool;
        }
    }

    private static ExecutorService getDownLoadInstance() {
        synchronized (lockobj) {
            if (mDownloadPool == null || mDownloadPool.isShutdown()) {
                mDownloadPool = Executors.newFixedThreadPool(3);
            }
            return mDownloadPool;
        }
    }

    private static ExecutorService getLogWriteInstance() {
        synchronized (lockobj) {
            if (mLogWritePool == null || mLogWritePool.isShutdown()) {
                mLogWritePool = Executors.newFixedThreadPool(3);
            }
            return mLogWritePool;
        }
    }

    private static ExecutorService getGameDownLoadInstance() {
        synchronized (lockobj) {
            if (mGameDownLoadPool == null || mGameDownLoadPool.isShutdown()) {
                mGameDownLoadPool = Executors.newFixedThreadPool(2);
            }
            return mGameDownLoadPool;
        }
    }

    public static ExecutorService getInstance(int priority) {
        if (priority == PRIORITY_HIGH) {
            return getHighInstance();
        } else if (priority == PRIORITY_MEDIUM) {
            return getMediumInstance();
        } else if (priority == PRIORITY_DOWN) {
            return getDownLoadInstance();
        } else if (priority == PRIORITY_WRITE_LOG) {
            return getLogWriteInstance();
        } else if (priority == PRIORITY_GAME_DOWNLOAD) {
            return getGameDownLoadInstance();
        } else {
            return getLowInstance();
        }
    }

    public static void shutdown() {
        if (mHighPool != null && !mHighPool.isShutdown()) {
            mHighPool.shutdown();
        }
        if (mMediumPool != null && !mMediumPool.isShutdown()) {
            mMediumPool.shutdown();
        }
        if (mLowPool != null && !mLowPool.isShutdown()) {
            mLowPool.shutdown();
        }
    }
}
