package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Box<Integer> box = new Box<>();

        Producer producer = new Producer(lock, box);
        Consumer consumer = new Consumer(lock, box);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Object lock;
        private Box<Integer> box;

        public Producer(Object lock, Box<Integer> box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!box.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    box.put(value);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Object lock;
        private Box<Integer> box;

        public Consumer(Object lock, Box<Integer> box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (box.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.println("Consuming " + box.take());
                    lock.notify();
                }
            }
        }
    }
}
