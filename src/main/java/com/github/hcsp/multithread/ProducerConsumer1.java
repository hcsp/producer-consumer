package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * wait() / notify()
 */
public class ProducerConsumer1 {
    static final Object lockObject = new Object();
    static AtomicReference<Integer> integerReference = new AtomicReference<>();

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
            for (int i = 0; i < 10; ++i) {
                synchronized (lockObject) {
                    while (integerReference.get() != null) {
                        try {
                            lockObject.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    integerReference.set(new Random().nextInt());
                    System.out.println("Producing " + integerReference.get());

                    lockObject.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                synchronized (lockObject) {
                    while (integerReference.get() == null) {
                        try {
                            lockObject.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + integerReference.get());
                    integerReference.set(null);

                    lockObject.notify();
                }
            }
        }
    }
}
