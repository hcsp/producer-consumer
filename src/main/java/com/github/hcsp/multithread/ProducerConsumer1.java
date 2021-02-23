package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    private static Integer test;
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Producer producer = new Producer(10, lock);
        Consumer consumer = new Consumer(lock);

        consumer.start();
        producer.start();

        producer.join();
        consumer.setFlag(false);
        consumer.join();
    }

    public static class Producer extends Thread {
        private Object lock;
        private int max;

        public Producer(int max, Object lock) {
            this.max = max;
            this.lock = lock;
        }

        @Override
        public void run() {
            Random r = new Random(System.currentTimeMillis());
            for (int i = 0; i < max; i++) {
                synchronized (lock) {
                    if (test != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int rInt = r.nextInt();
                    test = rInt;
                    System.out.println("Producing " + rInt);
                    lock.notifyAll();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Object lock;
        private boolean flag = true;

        public Consumer(Object lock) {
            this.lock = lock;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    synchronized (lock) {
                        if (test == null) {
                            lock.wait(10);
                            if (test == null) {
                                continue;
                            }
                        }
                        System.out.println("Consuming " + test);
                        test = null;
                        lock.notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
