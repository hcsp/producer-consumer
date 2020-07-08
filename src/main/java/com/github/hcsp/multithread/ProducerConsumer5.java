package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Exchanger
 */
public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();

        Producer producer = new Producer(exchanger);
        Consumer consumer = new Consumer(exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Exchanger<Integer> exchanger;

        public Producer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer value = new Random().nextInt();
                System.out.println("Producer " + value);
                try {
                    exchanger.exchange(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Integer> exchanger;

        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consumer " + exchanger.exchange(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
