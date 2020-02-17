package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    static Exchanger<Integer> exchanger = new Exchanger<Integer>();
    static Exchanger<Integer> signal = new Exchanger<Integer>();

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
                    Integer value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    exchanger.exchange(value);
                    signal.exchange(null);
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
                    int value = exchanger.exchange(null);
                    System.out.println("Consuming " + value);
                    signal.exchange(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

