package com.github.hcsp.multithread;

import java.util.Random;


/**
 * 生产者
 * @author wheelchen
 */
public class Producer extends Thread {
    private final Object lock;
    private static final int SIZE = 10;
    Producer(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < SIZE; i++) {
            synchronized (lock) {
                //已生产 等待消费
                while (Boss.flag) {
                    safeWait();
                }

                Boss.randomInt = getRandomInt();
                System.out.println("Producing " + Boss.randomInt);
                Boss.flag = true;
                lock.notifyAll();

            }
        }
    }

    private int getRandomInt() {
        return new Random().nextInt();
    }

    private void safeWait() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
