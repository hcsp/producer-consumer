package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Semaphore semaphore = new Semaphore(1, true); // 创建公平锁，当两个线程同时具备竞争锁的条件时，一人一次
        Producer producer = new Producer(container, semaphore);
        Consumer consumer = new Consumer(container, semaphore);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;
        private final Semaphore semaphore;

        public Producer(Container container, Semaphore semaphore) {
            this.container = container;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    if (!container.getValue().isPresent()) {
                        int r = new Random().nextInt();
                        System.out.println("Producing " + r);
                        container.setValue(Optional.of(r));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final Semaphore semaphore;

        public Consumer(Container container, Semaphore semaphore) {
            this.container = container;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    if (container.getValue().isPresent()) {
                        System.out.println("Consuming " + container.getValue().get());
                        container.setValue(Optional.empty());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    private static class Container {
        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        private Optional<Integer> value = Optional.empty();
    }
}
