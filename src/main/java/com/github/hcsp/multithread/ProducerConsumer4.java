package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Semaphore semCon = new Semaphore(0);
        Semaphore semProd = new Semaphore(1);

        Producer producer = new Producer(semProd, semCon, container);
        Consumer consumer = new Consumer(semProd, semCon, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Semaphore semProducer;
        Semaphore semConsumer;
        Container container;

        public Producer(Semaphore semProducer, Semaphore semConsumer, Container container) {
            this.semProducer = semProducer;
            this.semConsumer = semConsumer;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // acquiring the lock
                try {
                    semProducer.acquire();
                    int r = new Random().nextInt();
                    container.setValue(Optional.of(r));
                    System.out.println("Producing " + r);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                // release the lock
                semConsumer.release();
            }


        }
    }

    public static class Consumer extends Thread {
        Semaphore semProducer;
        Semaphore semConsumer;
        Container container;

        public Consumer(Semaphore semProducer, Semaphore semConsumer, Container container) {
            this.semProducer = semProducer;
            this.semConsumer = semConsumer;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // acquiring the lock
                try {
                    semConsumer.acquire();
                    Integer integer = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + integer);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                // release the lock
                semProducer.release();
            }


        }
    }

    public static class Container {
        Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
