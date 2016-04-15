package com.zhi.juc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/4/5.
 */
public class SimpleBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> bq = new SimpleBlockingQueue<Integer>(8);
        ExecutorService exec = Executors.newFixedThreadPool(2);

        demo_jackie_lock(bq, exec);
        demo_reentrant_lock(bq, exec);
    }

    public static void demo_reentrant_lock(SimpleBlockingQueue<Integer> bq, ExecutorService exec)
            throws InterruptedException {
        exec.execute(new Producer(bq));
        exec.execute(new Consumer(bq));
        TimeUnit.SECONDS.sleep(3);
        exec.shutdownNow();
    }

    /**
     * 这里只是简单的证明了自己定义的jackielock 是能够使用的相关的是能够使用的。
     */
    public static void demo_jackie_lock(SimpleBlockingQueue<Integer> bq, ExecutorService exec)
            throws InterruptedException {
        exec.execute(new Producer(bq));
        exec.execute(new Consumer(bq));
        TimeUnit.SECONDS.sleep(3);
        exec.shutdownNow();
    }

    public static class Producer implements Runnable {
        private final SimpleBlockingQueue<Integer> bq;
        private Random rnd = new Random(47);

        public Producer(SimpleBlockingQueue<Integer> bq) {
            this.bq = bq;
        }

        public void run() {
            try {
                int v;
                while (!Thread.interrupted()) {
                    v = rnd.nextInt(3);
                    bq.put(v);
                    System.out.println("P: " + v);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ok to exit.
            }
        }

    }

    public static class Consumer implements Runnable {
        private final SimpleBlockingQueue<Integer> bq;

        public Consumer(SimpleBlockingQueue<Integer> bq) {
            this.bq = bq;
        }

        public void run() {
            try {
                int v;
                while (!Thread.interrupted()) {
                    v = bq.take();
                    System.out.println("C " + v);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ok to exit.
            }
        }
    }
}
