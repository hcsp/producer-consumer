package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> container = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signal = new LinkedBlockingQueue<>(1);
        Producer producer = new Producer(container, signal);
        Consumer consumer = new Consumer(container, signal);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> container;
        BlockingQueue<Integer> signal;

        public Producer(BlockingQueue<Integer> container, BlockingQueue<Integer> signal) {
            this.container = container;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.put(r);
                    signal.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> container;
        BlockingQueue<Integer> signal;

        public Consumer(BlockingQueue<Integer> container, BlockingQueue<Integer> signal) {
            this.container = container;
            this.signal = signal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + container.take());
                    signal.put(0); // 可以put任意值
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
