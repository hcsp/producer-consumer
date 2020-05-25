package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(1);
        Container container = new Container(full, empty);


        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Container {
        Optional<Integer> value = Optional.empty();
        private Semaphore full;
        private Semaphore empty;

        Container(Semaphore full, Semaphore empty) {
            this.full = full;
            this.empty = empty;
        }

        public Semaphore getFull() {
            return full;
        }

        public Semaphore getEmpty() {
            return empty;
        }

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        private Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container.getEmpty().acquire();
                    int r = new Random().nextInt();
                    System.out.println("Producer " + r);
                    container.setValue(Optional.of(r));
                    container.getFull().release();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    static class Consumer extends Thread {

        private Container container;

        Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container.getFull().acquire();
                    System.out.println("Consumer " + container.getValue().get());
                    container.setValue(Optional.empty());
                    container.getEmpty().release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

