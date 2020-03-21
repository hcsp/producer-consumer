package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Plate plate = new Plate();


        Producer producer = new Producer(lock, plate);
        Consumer consumer = new Consumer(lock, plate);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Plate {
        private Optional value = Optional.empty();

        public void setValue(Optional value) {
            this.value = value;
        }

        public Optional getValue() {
            return value;
        }
    }

    public static class Producer extends Thread {
        private Object lock;
        private Plate plate;

        public Producer(Object lock, Plate plate) {
            this.lock = lock;
            this.plate = plate;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (plate.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Optional randomNumber = Optional.of(new Random().nextInt());
                    System.out.println("Producing " + randomNumber.get());
                    plate.setValue(randomNumber);
                    lock.notify();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        private Object lock;
        private Plate plate;

        public Consumer(Object lock, Plate plate) {
            this.lock = lock;
            this.plate = plate;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!plate.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + plate.getValue().get());
                    plate.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}
