package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> storeHouse = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signal = new LinkedBlockingQueue<>(1);
        Producer producer = new Producer(storeHouse, signal);
        Consumer consumer = new Consumer(storeHouse, signal);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> storeHouse;
        BlockingQueue<Integer> signal;

        public Producer(BlockingQueue<Integer> storeHouse, BlockingQueue<Integer> signal) {
            this.storeHouse = storeHouse;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                try {
                    storeHouse.put(r);
                    System.out.println("Producing " + r);
                    signal.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> storeHouse;
        BlockingQueue<Integer> signal;

        public Consumer(BlockingQueue<Integer> storeHouse, BlockingQueue<Integer> signal) {
            this.storeHouse = storeHouse;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + storeHouse.take());
                    signal.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}