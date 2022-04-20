package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

// Exchanger
public class ProducerConsumer5 {
    static Exchanger<Integer> exchanger = new Exchanger<>();

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
                Random random = new Random();
                int intProduced = random.nextInt();
                safeExchange(intProduced);
                System.out.println("Producing " + intProduced);
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer intConsumed = safeExchange(0);
                System.out.println("Consuming " + intConsumed);
            }
        }
    }

    private static Integer safeExchange(int intProduced) {
        try {
            return exchanger.exchange(intProduced);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
