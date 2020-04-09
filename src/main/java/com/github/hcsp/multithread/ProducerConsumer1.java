package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static Object lock = new Object();
    public static Container container = new Container();
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
            for (int i = 0; i < 10; i++) {

                synchronized (lock) {
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int value = new Random().nextInt();
                    container.setValue(Optional.of(value));
                    System.out.println("Producing " + value);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                synchronized (lock) {
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);
                    lock.notify();
                }
            }
        }
    }

}
