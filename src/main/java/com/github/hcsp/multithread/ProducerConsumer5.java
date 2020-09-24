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

    private static final Exchanger<Integer> EXCHANGER = new Exchanger<>();
    private static final Exchanger<Integer> TEMP_EXCHANGER = new Exchanger<>();

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    TEMP_EXCHANGER.exchange(0);
                    int num = new Random().nextInt();
                    System.out.println("Producing :" + num);
                    EXCHANGER.exchange(num);
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
                    TEMP_EXCHANGER.exchange(0);
                    System.out.println("Consuming :" + EXCHANGER.exchange(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
