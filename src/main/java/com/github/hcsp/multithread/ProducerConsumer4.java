package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    private static Semaphore producerSemaphore = new Semaphore(0);
    private static Semaphore consumerSemaphore = new Semaphore(1);
    private static Integer pro;
    public static void main(String[] args) throws InterruptedException {


        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                producerSemaphore.acquire();
                int r = new Random().nextInt();
                System.out.println("producing " + r);
                pro = r;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                consumerSemaphore.release();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                consumerSemaphore.acquire();
                System.out.println("Consuming " + pro);
                pro = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                producerSemaphore.release();
            }
        }
    }
}
