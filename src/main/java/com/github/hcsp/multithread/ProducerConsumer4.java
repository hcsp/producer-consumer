package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore producerSemaphore = new Semaphore(1);
        Semaphore consumerSemaphore = new Semaphore(0);
        Container container = new Container();

        Producer producer = new Producer(producerSemaphore, consumerSemaphore, container);
        Consumer consumer = new Consumer(producerSemaphore, consumerSemaphore, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Semaphore producerSemaphore;
        Semaphore consumerSemaphore;
        Container container;

        public Producer(Semaphore ProducerSemaphore, Semaphore ConsumerSemaphore, Container container) {
            this.producerSemaphore = ProducerSemaphore;
            this.consumerSemaphore = ConsumerSemaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    producerSemaphore.acquire();
                    int tempInt = new Random().nextInt();
                    System.out.println("Producing " + tempInt);
                    container.setValue(Optional.of(tempInt));
                    consumerSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Semaphore producerSemaphore;
        Semaphore consumerSemaphore;
        Container container;

        public Consumer(Semaphore producerSemaphore, Semaphore consumerSemaphore, Container container) {
            this.producerSemaphore = producerSemaphore;
            this.consumerSemaphore = consumerSemaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    consumerSemaphore.acquire();
                    int tempInt = container.getValue().get();
                    System.out.println("Consuming " + tempInt);
                    container.setValue(Optional.empty());
                    producerSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
