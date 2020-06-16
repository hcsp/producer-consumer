package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static Semaphore semCon = new Semaphore(0);
    static Semaphore semProd = new Semaphore(1);
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semProd.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int nextInt = new Random().nextInt();
                container.setValue(Optional.of(nextInt));
                System.out.println("Producing " + nextInt);
                semCon.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semCon.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer value = container.getValue().get();
                System.out.println("Consuming " + value);
                container.setValue(Optional.empty());
                semProd.release();
            }
        }
    }
}
