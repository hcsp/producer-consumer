package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    private static Integer container;

    // Semaphore
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        Producer producer = new Producer(semaphore);
        Consumer consumer = new Consumer(semaphore);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Semaphore semaphore;

        public Producer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    while (container != null) {
                        semaphore.release();
                    }
                    int rNum = new Random().nextInt();
                    System.out.println("Producing " + rNum);
                    container = rNum;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Semaphore semaphore;

        public Consumer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    while (container == null) {
                        semaphore.release();
                    }
                    System.out.println("Consuming " + container);
                    container = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }
}
