package com.zhi.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class BlockingQueue<T> {

    private List<T> data;
    private final int size;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition emptyCondition = lock.newCondition();
    private final Condition fullCondition = lock.newCondition();
    private int putNums;

    public BlockingQueue(int size){
        this.size = size;
        data = new ArrayList<T>(size);
    }

    public void put(T v) throws InterruptedException {
        lock.lock();
        try{
            while(putNums == size){
                fullCondition.await();
            }
            data.add(v);
            putNums++;
            emptyCondition.signalAll();
        }finally{
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try{
            while(putNums == 0){
                emptyCondition.await();
            }
            putNums--;
            fullCondition.signalAll();
            return data.remove(0);
        }finally{
            lock.unlock();
        }
    }
}
