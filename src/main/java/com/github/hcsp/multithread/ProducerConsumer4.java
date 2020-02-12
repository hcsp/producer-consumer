package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {


        Semaphore mutex = new Semaphore(1);
        Container container = new Container();

        Producer producer = new Producer(mutex, container);
        Consumer consumer = new Consumer(mutex, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Semaphore mutex;
        Container container;

        public Producer(Semaphore mutex, Container container) {
            this.mutex = mutex;
            this.container = container;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    while (container.getContainer().isPresent()) {
                    }
                    mutex.acquire();
                    Integer r = new Random().nextInt();
                    System.out.println("product:" + r);
                    container.setContainer(Optional.of(r));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                }
            }
        }

    }

    public static class Consumer extends Thread {
        Semaphore mutex;
        Container container;

        public Consumer(Semaphore mutex, Container container) {
            this.mutex = mutex;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    while (!container.getContainer().isPresent()) {
                    }
                    mutex.acquire();
                    System.out.println("consumer:" + container.getContainer().get());
                    container.setContainer(Optional.empty());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();

        public Optional getContainer() {
            return container;
        }

        public void setContainer(Optional container) {
            this.container = container;
        }
    }
}
