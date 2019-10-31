package com.github.hcsp.multithread;

/**
 * @author wheelchen
 */
public class Consumer extends Thread {
    private final Object lock;
    private static final int SIZE = 10;

    Consumer(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        for (int i = 0; i < SIZE; i++) {
            synchronized (lock) {
                //等待生产
                while (!Boss.flag) {
                    safeWait();
                }

                System.out.println("Consuming " + Boss.randomInt);
                Boss.flag = false;
                lock.notifyAll();

            }
        }
    }

    private void safeWait() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
