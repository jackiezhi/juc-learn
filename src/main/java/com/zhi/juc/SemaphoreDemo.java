package com.zhi.juc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jackiezhi on 2016/4/15.
 */
public class SemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        final DbConnectionManager connManager = new DbConnectionManager();
        ExecutorService exec = Executors.newFixedThreadPool(6);
        for(int i=0; i<100; i++){
            exec.execute(new Runnable() {
                public void run() {
                    connManager.useConnection();
                }
            });
        }

        TimeUnit.SECONDS.sleep(6);
        exec.shutdownNow();
    }

    private static class DbConnectionManager {
        private final JackieSemaphore semaphore = new JackieSemaphore(3);
        private final Random rand = new Random(47);

        public void useConnection() {
            semaphore.acquire();
            String threadName = Thread.currentThread().getName();
            try {
                System.out.println(threadName + "   acquireDbConnection.");
                //模拟应用使用过数据库连接的时间
                TimeUnit.SECONDS.sleep(rand.nextInt(3) + 1);
            } catch (InterruptedException e) {
            } finally {
                System.out.println(threadName + "   releaseDbConnection.");
                semaphore.release();
            }
        }
    }

}
