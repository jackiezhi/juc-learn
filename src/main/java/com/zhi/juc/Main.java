package com.zhi.juc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService exec = Executors.newFixedThreadPool(2);
        BlockingQueue<Integer> data = new BlockingQueue<Integer>(16);
        exec.execute(new Producer(data, 32));
        exec.execute(new Consumer(data));

        TimeUnit.SECONDS.sleep(3);
        exec.shutdownNow();
    }

    public static class Producer implements Runnable {
        private final BlockingQueue<Integer> data;
        private int repeats;
        private int num;
        private Random rnd = new Random(47);

        public Producer(BlockingQueue<Integer> data, int repeats) {
            this.data = data;
            this.repeats = repeats;
        }

        public void run() {
            try {
                int v;
                while (!Thread.interrupted() && repeats-- > 0) {
                    v = rnd.nextInt(3);
                    data.put(v);
                    num += v;
                    System.out.println("P: " + v);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ok to exit.
            } finally {
                System.out.println("P num: " + num);
            }
        }

    }

    public static class Consumer implements Runnable {
        private final BlockingQueue<Integer> data;

        private int num;

        public Consumer(BlockingQueue<Integer> data) {
            this.data = data;
        }

        public void run() {
            try {
                int v;
                while (!Thread.interrupted()) {
                    v = data.take();
                    num += v;
                    System.out.println("C " + v);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //ok to exit.
            } finally {
                System.out.println("C num: " + num);
            }
        }
    }
}
