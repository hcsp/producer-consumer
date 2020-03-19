package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

/**
 *  Exchanger
 */
public class ProducerConsumer4 {

    public static void main(String[] args) throws InterruptedException {
        Exchanger<Object> exchanger = new Exchanger();

        Producer producer = new Producer(exchanger);
        Consumer consumer = new Consumer(exchanger);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        final Exchanger<Object> exchanger;

        Producer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Object value = new Random().nextInt();
                    exchanger.exchange(value);
                    System.out.println("Producing " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final Exchanger exchanger;

        Consumer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Object value = exchanger.exchange(null);
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
