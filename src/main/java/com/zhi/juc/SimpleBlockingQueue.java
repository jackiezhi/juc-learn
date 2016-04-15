package com.zhi.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class SimpleBlockingQueue<T> {
    private List<T> items;
    private final int size;
    //默认使用j.u.c 中的ReentrantLock
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public SimpleBlockingQueue(int size) {
        this.size = size;
        items = new ArrayList<T>(size);
    }

    public SimpleBlockingQueue(int size, Lock lock){
        this(size);
        //方便使用自定义的JackieLock
        this.lock = lock;
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void put(T v) throws InterruptedException {
        lock.lock();
        try {
            while (items.size() == size) {
                notFull.await();
            }
            items.add(v);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (items.size() == 0) {
                notEmpty.await();
            }
            notFull.signalAll();
            return items.remove(0);
        } finally {
            lock.unlock();
        }
    }
}
