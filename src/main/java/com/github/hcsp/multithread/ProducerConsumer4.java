package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();

        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                container.put();
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                container.get();
            }
        }
    }

    protected static class Container {
        private Semaphore canProduce = new Semaphore(1);
        private Semaphore canConsume = new Semaphore(0);
        private Random random = new Random();
        private List<Integer> stock = new LinkedList<>();

        public void put() {
            try {
                canProduce.acquire();
                int value = random.nextInt();
                stock.add(value);
                System.out.println("Producing " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                canConsume.release();
            }
        }

        public void get() {
            try {
                canConsume.acquire();
                int value = stock.remove(0);
                System.out.println("Consuming " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                canProduce.release();
            }
        }
    }
}
