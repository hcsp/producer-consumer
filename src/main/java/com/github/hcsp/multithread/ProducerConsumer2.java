package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Plate plate = new Plate(lock);

        Producer producer = new Producer(lock, plate);
        Consumer consumer = new Consumer(lock, plate);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Plate {
        private Optional value = Optional.empty();
        private Condition notConsumingYet;
        private Condition notProducingYet;

        public Condition getNotConsumingYet() {
            return notConsumingYet;
        }

        public Condition getNotProducingYet() {
            return notProducingYet;
        }

        public Plate(ReentrantLock lock) {
            notConsumingYet = lock.newCondition();
            notProducingYet = lock.newCondition();
        }

        public void setValue(Optional value) {
            this.value = value;
        }

        public Optional getValue() {
            return value;
        }
    }

    public static class Producer extends Thread {
        private ReentrantLock lock;
        private Plate plate;

        public Producer(ReentrantLock lock, Plate plate) {
            this.lock = lock;
            this.plate = plate;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (plate.getValue().isPresent()) {
                        try {
                            plate.getNotConsumingYet().signal();
                            plate.getNotProducingYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Optional randomNumber = Optional.of(new Random().nextInt());
                    System.out.println("Producing " + randomNumber.get());
                    plate.setValue(randomNumber);
                    plate.getNotProducingYet().signalAll();
                } finally {
                    lock.unlock();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        private ReentrantLock lock;
        private Plate plate;

        public Consumer(ReentrantLock lock, Plate plate) {
            this.lock = lock;
            this.plate = plate;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!plate.getValue().isPresent()) {
                        try {
                            plate.getNotProducingYet().signal();
                            plate.getNotConsumingYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + plate.getValue().get());
                    plate.setValue(Optional.empty());
                    plate.getNotConsumingYet().signalAll();
                } finally {
                    lock.unlock();
                }
            }

        }
    }
}
