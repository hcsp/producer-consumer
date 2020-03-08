package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore sem = new Semaphore(1);
        Producer producer = new Producer(sem);
        Consumer consumer = new Consumer(sem);
        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Shared {
        static int sharedNum = 0;

        static void produceNum() {
            sharedNum = new Random().nextInt();
            System.out.println("Producing " + sharedNum);
        }

        static void consumeNum() {
            System.out.println("Consuming " + sharedNum);
        }
    }

    public static class Producer extends Thread {
        private Semaphore sem;

        Producer(Semaphore sem) {
            this.sem = sem;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    sem.acquire();
                    Shared.produceNum();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                sem.release();
            }
        }
    }

    public static class Consumer extends Thread {
        private Semaphore sem;

        Consumer(Semaphore sem) {
            this.sem = sem;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    sem.acquire();
                    Shared.consumeNum();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                sem.release();
            }
        }
    }
}
