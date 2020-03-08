package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    private static Exchanger<Integer> exchanger = new Exchanger<>();

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
                try {
                    int randomNum = new Random().nextInt();
                    System.out.println("Producing " + randomNum);
                    exchanger.exchange(randomNum);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer randomNum = exchanger.exchange(0);
                    System.out.println("Consuming " + randomNum);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
