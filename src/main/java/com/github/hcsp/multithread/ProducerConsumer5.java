package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.function.Consumer;

public class ProducerConsumer5 {
    //Exchanger
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
                int r = new Random().nextInt();
                try {
                    exchanger.exchange(r);
                    System.out.println("Producing" + " " + r);
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
                    Integer value = exchanger.exchange(null);
                    System.out.println("Consuming" +  " " +value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



