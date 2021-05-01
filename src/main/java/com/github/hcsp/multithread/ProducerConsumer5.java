package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

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
        private Exchanger<Integer> exchanger;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                    Integer value = new Random().nextInt();
                    exchanger.exchange(value);
                    System.out.println("Producing " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public Producer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }
    }

    public static class Consumer extends Thread {
        private Exchanger<Integer> exchanger;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                    Integer value = exchanger.exchange(0);
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public Consumer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }
    }

}
