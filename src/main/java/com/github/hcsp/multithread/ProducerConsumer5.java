package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    static Exchanger<Integer> exchange = new Exchanger<>();


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
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                Integer num = new Random().nextInt();
                System.out.println("Producing " + num);
                count++;
                try {
                    exchange.exchange(num);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                count++;
                try {
                    System.out.println("Consuming " + exchange.exchange(null));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
