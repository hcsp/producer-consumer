package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Semaphore available = new Semaphore(1, true);
        Producer producer = new Producer(container, available);
        Consumer consumer = new Consumer(container, available);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        Semaphore available;

        public Producer(Container container, Semaphore available) {
            this.container = container;
            this.available = available;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    available.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (container.getContainer().isPresent()) {
                    available.release();
                }
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.setContainer(Optional.of(r));
                available.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Semaphore available;

        public Consumer(Container container, Semaphore available) {
            this.container = container;
            this.available = available;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    available.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (container.getContainer().isPresent()) {
                    Integer value = container.getContainer().get();
                    System.out.println("Consuming " + value);
                    container.setContainer(Optional.empty());
                }
                available.release();
            }
        }
    }
}
