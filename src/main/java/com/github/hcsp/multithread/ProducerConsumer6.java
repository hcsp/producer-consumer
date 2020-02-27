package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CountDownLatch
 */
public class ProducerConsumer6 {
    static CountDownLatch full = new CountDownLatch(1);
    static CountDownLatch empty = new CountDownLatch(1);
    static AtomicReference<Integer> integerReference = new AtomicReference<>();

    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer5.Producer producer = new ProducerConsumer5.Producer();
        ProducerConsumer5.Consumer consumer = new ProducerConsumer5.Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    empty.await();
                    empty = new CountDownLatch(1);

                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    integerReference.set(num);

                    full.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    full.await();
                    full = new CountDownLatch(1);

                    System.out.println("Consuming " + integerReference.get());
                    integerReference.set(null);

                    empty.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
