package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

/* Exchanger */
public class ProducerConsumer5 {

    public static class ValueObject {
        public static int value = 0;
    }

    static Exchanger<Integer> exchanger = new Exchanger<Integer>();

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer(exchanger);
        Consumer consumer = new Consumer(exchanger);

        for (int i = 0; i < 10; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }

    public static class Producer extends Thread {

        Exchanger<Integer> exchanger;

        public Producer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            try {
                ValueObject.value = new Random().nextInt();
                System.out.println("Producing " + ValueObject.value);
                exchanger.exchange(ValueObject.value);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            try {
                System.out.println("Consuming " + exchanger.exchange(ValueObject.value));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
