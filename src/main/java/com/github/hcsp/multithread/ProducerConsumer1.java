package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> value = new ArrayList<>();
        Object lock = new Object();
        Producer producer = new Producer(value, lock);
        Consumer consumer = new Consumer(value, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        List<Integer> value;
        Object lock;

        public Producer(List<Integer> value, Object lock) {
            this.value = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!value.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int j = new Random().nextInt();
                    System.out.println("Producing " + j);
                    value.add(j);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        List<Integer> value;
        Object lock;

        public Consumer(List<Integer> value, Object lock) {
            this.value = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (value.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int num = value.get(0);
                    System.out.println("Consuming " + num);
                    value.clear();
                    lock.notify();
                }
            }
        }
    }
}
