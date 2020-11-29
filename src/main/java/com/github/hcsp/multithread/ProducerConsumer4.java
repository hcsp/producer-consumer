package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {

    static Semaphore empty = new Semaphore(1);
    static Semaphore full = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> storeHouse = new LinkedList<>();

        Producer producer = new Producer(storeHouse);
        Consumer consumer = new Consumer(storeHouse);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Queue<Integer> storeHouse;

        public Producer(Queue<Integer> storeHouse) {
            this.storeHouse = storeHouse;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                try {
                    empty.acquire();
                    mutex.acquire();
                    storeHouse.add(r);
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    full.release(1);
                    mutex.release(1);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Queue<Integer> storeHouse;

        public Consumer(Queue<Integer> storeHouse) {
            this.storeHouse = storeHouse;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    full.acquire();
                    mutex.acquire();
                    System.out.println("Consuming " + storeHouse.remove());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    empty.release(1);
                    mutex.release(1);
                }
            }
        }
    }
}
