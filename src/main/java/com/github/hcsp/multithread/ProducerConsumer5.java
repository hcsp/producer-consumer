package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    static Exchanger<Integer> exchanger = new Exchanger<>();
    static Exchanger<Integer> exchanger1 = new Exchanger<>();


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
                int random = new Random().nextInt();
                System.out.println("Producing " + random);
                try {
                    exchanger.exchange(random);
                    exchanger1.exchange(0);
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
                    int random = exchanger.exchange(null);
                    System.out.println("Consuming " + random);
                    exchanger1.exchange(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


