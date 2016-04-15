package com.zhi.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gouqi<gouqi@2dfire.com>
 * @date 2016/3/28.
 */
public class Main {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Semaphore semaphore = new Semaphore(8);
    }

}
