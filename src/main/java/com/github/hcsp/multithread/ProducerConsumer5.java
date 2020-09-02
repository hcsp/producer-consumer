package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final Exchanger<Integer> exchanger = new Exchanger<>();
    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    exchanger.exchange(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + exchanger.exchange(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
