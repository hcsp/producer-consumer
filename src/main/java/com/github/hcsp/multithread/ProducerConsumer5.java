package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    static Exchanger exchanger = new Exchanger();
    static Container container = new Container();
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
                int value = new Random().nextInt();
                container.setValue(Optional.of(value));
                try {
                    exchanger.exchange(container);
                    System.out.println("Producer " + value);
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
                    Container exchange = (Container) exchanger.exchange(container);
                    System.out.println("Consumer " + exchange.getValue().get());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
