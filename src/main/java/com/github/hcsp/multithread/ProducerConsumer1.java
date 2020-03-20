package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Box box = new Box();
        Object lock = new Object();
        Producer producer = new Producer(box, lock);
        Consumer consumer = new Consumer(box, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Box {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        Box box;
        final Object lock;

        public Producer(Box box, Object lock) {
            this.box = box;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (box.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    box.setValue(Optional.of(r));
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Box box;
        final Object lock;

        public Consumer(Box box, Object lock) {
            this.box = box;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!box.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + box.getValue().get());
                    box.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}
