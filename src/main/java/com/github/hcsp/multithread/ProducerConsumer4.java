package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Semaphore semaphore = new Semaphore(10, true);
        Producer producer = new Producer(semaphore, container);
        Consumer consumer = new Consumer(semaphore, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Semaphore semaphore;
        private Container container;

        public Producer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.getSharedData().isPresent()) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int random = new Random().nextInt();
                System.out.println("Producing " + random);
                container.setSharedData(Optional.of(random));
                semaphore.release();
            }
        }
    }

    public static class Consumer extends Thread {
        private Semaphore semaphore;
        private Container container;

        public Consumer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (!container.getSharedData().isPresent()) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int value = container.getSharedData().get();
                System.out.println("Consuming " + value);
                container.setSharedData(Optional.empty());
                semaphore.release();
            }
        }
    }

    public static class Container {

        private Optional<Integer> sharedData = Optional.empty();

        public Optional<Integer> getSharedData() {
            return sharedData;
        }

        public void setSharedData(Optional<Integer> sharedData) {
            this.sharedData = sharedData;
        }
    }
}
