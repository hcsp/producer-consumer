package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();

        Producer producer = new Producer(10, exchanger);
        Consumer consumer = new Consumer(exchanger);

        producer.setName("Producing ");
        consumer.setName("Consuming ");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private int max;
        private Exchanger<Integer> exchanger;

        public Producer(int max, Exchanger<Integer> exchanger) {
            this.max = max;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < max; i++) {
                Random r = new Random();
                int rInt = r.nextInt();
                try {
                    System.out.println(getName() + rInt);
                    exchanger.exchange(rInt);
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        private Exchanger<Integer> exchanger;

        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Integer data = exchanger.exchange(0, 100, TimeUnit.MILLISECONDS);
                    System.out.println(getName() + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
//                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
