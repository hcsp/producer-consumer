package com.github.hcsp.multithread;


import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphore = new Semaphore(1);
        Container container = new Container();
        Producer producer = new Producer(semaphore, container);
        Consumer consumer = new Consumer(semaphore, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Semaphore semaphore;
        Container container;

        public Producer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override

        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (container.getValue().isPresent()) {
                    semaphore.release();
                }
                int r = new Random().nextInt();
                System.out.println("Producing" + r);
                container.setValue(Optional.of(r));
                semaphore.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Semaphore semaphore;
        Container container;

        public Consumer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!container.getValue().isPresent()) {
                    semaphore.release();
                }
                Integer value = container.getValue().get();
                container.setValue(Optional.empty());
                System.out.println("Consumer" + value);
                semaphore.release();
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
