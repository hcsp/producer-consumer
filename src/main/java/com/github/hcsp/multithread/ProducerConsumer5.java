package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<>();

        new Producer(exchanger).start();
        new Consumer(exchanger).start();
    }

    public static class Producer extends Thread {
        Exchanger<Integer> exchanger;

        Producer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                int value = new Random().nextInt();
                try {
                    System.out.println("Producing:" + value);
                    exchanger.exchange(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Integer> exchanger;

        Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {

            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(100);
                    Integer exchange = exchanger.exchange(null);
                    System.out.println("Consuming " + exchange);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
