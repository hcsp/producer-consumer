package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static Container container = new Container();
    static Semaphore semaphoreProducer = new Semaphore(1);
    static Semaphore semaphoreConsumer = new Semaphore(0);
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
                try {
                    semaphoreProducer.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int value = new Random().nextInt();
                container.setValue(Optional.of(value));
                System.out.println("Producer " + value);
                semaphoreConsumer.release();
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreConsumer.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consumer " + container.getValue().get());
                semaphoreProducer.release();
            }
        }
    }
}
