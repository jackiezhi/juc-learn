package com.zhi.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class SimpleBlockingQueue<T> {

    private List<T> items;
    private final int size;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition emptyCondition = lock.newCondition();
    private final Condition fullCondition = lock.newCondition();

    public SimpleBlockingQueue(int size) {
        this.size = size;
        items = new ArrayList<T>(size);
    }

    public void put(T v) throws InterruptedException {
        lock.lock();
        try {
            while (items.size() == size) {
                fullCondition.await();
            }
            items.add(v);
            emptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (items.size() == 0) {
                emptyCondition.await();
            }
            fullCondition.signalAll();
            return items.remove(0);
        } finally {
            lock.unlock();
        }
    }
}
