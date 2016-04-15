package com.zhi.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by jackiezhi on 2016/4/15.
 */
public class LockDemo {
    public static void main(String[] args) throws InterruptedException {
        final Shared shared = new Shared();

        Thread t = new Thread(new Runnable() {
            public void run() {
                shared.fun();
            }
        });

        shared.selfLock();
        t.start();
        System.out.println("everything ok.");
        TimeUnit.SECONDS.sleep(3);
        shared.selfUnlock();

    }

    private static class Shared {
        private Lock lock = new JackieLock();

        public void selfLock() {
            lock.lock();
        }

        public void selfUnlock() {
            lock.unlock();
        }


        public void fun() {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " enter.");
            } finally {
                lock.unlock();
            }
        }
    }

    //condition queue
}
