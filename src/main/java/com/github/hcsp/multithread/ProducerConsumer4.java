package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Semaphore signal = new Semaphore(2);

        Producer producer = new Producer(container, signal);
        Consumer consumer = new Consumer(container, signal);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        Semaphore signal;

        public Producer(Container container, Semaphore signal) {
            this.signal = signal;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.getValue().isPresent()) {
                    try {
                        signal.acquire(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing" + r);
                container.setValue(Optional.of(r));
                signal.release(1);
            }

        }
    }

    public static class Consumer extends Thread {
        Semaphore signal;
        Container container;

        public Consumer(Container container, Semaphore signal) {
            this.signal = signal;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (!container.getValue().isPresent()) {
                    try {
                        signal.acquire(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consuming" + container.getValue().get());
                container.setValue(Optional.empty());
                signal.release(0);
            }
        }
    }


    private static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
