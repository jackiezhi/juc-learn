package com.zhi.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class MainTest {

    //lock.park() 会一直阻塞线程，Thread.currentThread.unpark() 唤醒当前的线程。
    public static void main(String[] args) throws InterruptedException {

        ExecutorService exec = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();

        new Thread(new SleepThread(lock)).start();

        TimeUnit.SECONDS.sleep(1);
        exec.execute(new LockGetter(lock));
    }

    public static class SleepThread implements Runnable{
        private ReentrantLock lock;
        public SleepThread(ReentrantLock lock){
            this.lock = lock;
        }
        public void run() {
            lock.lock();
            try{
                System.out.println("enter lock");
                //阻塞当前线程
                LockSupport.park();
                System.out.println("leave lock");
            }finally{
                lock.unlock();
            }
        }
    }

    public static class LockGetter implements Runnable{
        private ReentrantLock lock;
        public LockGetter(ReentrantLock lock){
            this.lock = lock;
        }
        public void run() {
            lock.lock();
            try{
                System.out.println("zzzz");
            }finally{
                lock.unlock();
            }
        }
    }
}
