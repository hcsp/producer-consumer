package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {

    private static Exchanger<Integer> exchanger;

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
            int r = new Random().nextInt();
            System.out.println("Producing " + r);
            try {
                exchanger.exchange(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                int value = exchanger.exchange(null);
                System.out.println("Consuming " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
