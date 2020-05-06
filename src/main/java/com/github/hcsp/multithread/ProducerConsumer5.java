package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Exchanger
 *
 * @author kwer
 * @date 2020/5/6 23:20
 */
public class ProducerConsumer5 {
    private static Exchanger<Integer> exchanger = new Exchanger<Integer>();

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
                int j = new Random().nextInt();
                System.out.println("Producing " + j);
                try {
                    exchanger.exchange(j);
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
