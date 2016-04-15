package com.zhi.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by jackiezhi on 2016/4/15.
 */
public class JackieLock implements Lock {

    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(0);
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public void unlock() {
        sync.release(0);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }


    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            int state = getState();
            if(state == 0){
                return compareAndSetState(0, 1);
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            int state = getState();
            if(state == 1){
                return compareAndSetState(1, 0);
            }
            return false;
        }

        private ConditionObject newCondition(){
            return new ConditionObject();
        }
    }
}
