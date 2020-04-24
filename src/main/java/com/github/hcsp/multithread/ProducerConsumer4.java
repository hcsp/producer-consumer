package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/* Semaphore */
public class ProducerConsumer4 {

    public static class ValueObject {
        public static int value = 0;
    }

    static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer(semaphore);
        Consumer consumer = new Consumer(semaphore);

        for (int i = 0; i < 10; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }

    public static class Producer extends Thread {

        Semaphore semaphore;

        public Producer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                ValueObject.value = new Random().nextInt();
                System.out.println("Producing " + ValueObject.value);
                semaphore.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {

        Semaphore semaphore;

        public Consumer(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("Consuming " + ValueObject.value);
                semaphore.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
