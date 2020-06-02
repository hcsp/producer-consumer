package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Integer> container = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final LinkedBlockingQueue<Integer> container;

        public Producer(LinkedBlockingQueue<Integer> container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer r = new Random().nextInt();
                    container.put(r);
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static class Consumer extends Thread {
        private final LinkedBlockingQueue<Integer> container;

        public Consumer(LinkedBlockingQueue<Integer> container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer r = container.take();
                    System.out.println("Consuming " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
