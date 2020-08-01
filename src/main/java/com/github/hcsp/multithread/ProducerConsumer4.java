package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(10);
        Container container = new Container();
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
                try {
                    semaphore.acquire();
                    while (container.getValue().isPresent()) {
                    }
                    int randomNumber = new Random().nextInt();
                    System.out.println("Producing " + randomNumber);
                    container.setValue(Optional.of(randomNumber));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
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
                try {
                    semaphore.acquire();
                    while (!container.getValue().isPresent()) {
                    }
                    System.out.println("Consuming " + container.getValue().get());
                    container.setValue(Optional.empty());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();

                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
