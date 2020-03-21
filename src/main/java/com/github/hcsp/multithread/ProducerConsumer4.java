package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore producerLock = new Semaphore(1);
        Semaphore consumerLock = new Semaphore(0);
        Plate plate = new Plate();

        Producer producer = new Producer(producerLock, consumerLock, plate);
        Consumer consumer = new Consumer(producerLock, consumerLock, plate);

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
        private Semaphore producerLock;
        private Semaphore consumerLock;
        private Plate plate;

        public Producer(Semaphore producerLock, Semaphore consumerLock, Plate plate) {
            this.producerLock = producerLock;
            this.consumerLock = consumerLock;
            this.plate = plate;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    producerLock.acquire();
                    Optional randomNumber = Optional.of(new Random().nextInt());
                    plate.setValue(randomNumber);
                    System.out.println("Producing " + randomNumber.get());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    consumerLock.release();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        private Semaphore producerLock;
        private Semaphore consumerLock;
        private Plate plate;

        public Consumer(Semaphore producerLock, Semaphore consumerLock, Plate plate) {
            this.producerLock = producerLock;
            this.consumerLock = consumerLock;
            this.plate = plate;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    consumerLock.acquire();
                    System.out.println("Consuming " + plate.getValue().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    producerLock.release();
                }
            }

        }
    }
}
