package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer5 {
        public static void main(String[] args) throws InterruptedException {
            Semaphore available = new Semaphore(1, true);
            Container container = new Container();

            Producer producer = new Producer(available, container);
            Consumer consumer = new Consumer(available, container);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
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

    public static class Producer extends Thread {
        Semaphore available;
        Container container;

        public Producer(Semaphore available, Container container) {
            this.available = available;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.getValue().isPresent()) {
                    try {
                        available.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.setValue(Optional.of(r));
                available.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Semaphore available;
        Container container;

        public Consumer(Semaphore available, Container container) {
            this.available = available;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (!container.getValue().isPresent()) {
                    try {
                        available.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Producing " + container.getValue().get());
                container.setValue(Optional.empty());
                available.release();
            }
        }
    }
}
