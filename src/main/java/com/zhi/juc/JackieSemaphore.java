package com.zhi.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by jackiezhi on 2016/4/15.
 */
public class JackieSemaphore {

    private final Sync sync;
    public JackieSemaphore(int permits){
        sync = new Sync(permits);
    }
    /**
     * Semaphore中重要的方法是acquire/release (获取permits，归还permits)
     * 不响应中断
     */
    public void acquire(){
        sync.acquireShared(1);
    }

    public void release(){
        sync.releaseShared(1);
    }

    private static class Sync extends AbstractQueuedSynchronizer{

        public Sync(int permits){
            setState(permits);
        }
        /**
         * 简单实现，并没有利用到入参
         * @param arg
         * @return remaining
         */
        @Override
        protected int tryAcquireShared(int arg) {
            for(;;){
                int avaliable = getState();
                int remaining = avaliable - 1;
                if(remaining < 0 ||
                        compareAndSetState(avaliable, remaining)){
                    return remaining;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for(;;){
                int current = getState();
                int next = current + 1;
                if(compareAndSetState(current, next)){
                    return true;
                }
            }
        }
    }
}
