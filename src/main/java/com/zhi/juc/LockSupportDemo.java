package com.zhi.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by jackiezhi on 2016/4/14.
 * LockSupport 提供的方法主要是park/unpark 以及相关的重载的方法。底层使用到了native方法实现。
 */
public class LockSupportDemo {
    public static void main(String[] args){

        Thread t = new Thread(new Runnable() {
            public void run() {
                LockSupport.parkNanos(Long.MAX_VALUE);
                System.out.println("interrupted? "+Thread.interrupted());
            }
        });

        t.start();
        LockSupport.unpark(t);
    }

}
