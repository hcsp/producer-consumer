package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer5 {
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
            for (int j = 0; j < 10; j++) {
                try {
                    container.semaphore.acquire();
                    while (!container.getOptional().isPresent()) {
                        int r = new Random().nextInt();
                        System.out.println("Producing " + r);
                        container.setOptional(Optional.of(r));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    container.semaphore0.release();
                }

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
            for (int j = 0; j < 10; j++) {
                try {

                    while (!container.getOptional().isPresent()){
                        container.semaphore0.acquire();
                    }
                    Integer a = container.getOptional().get();
                    System.out.println("Consuming " + a);
                    container.setOptional(Optional.empty());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    container.semaphore.release();
                }
            }
        }
    }
    static class Container {
        private Optional<Integer> optional = Optional.empty();
        Semaphore semaphore =new Semaphore(1);
        Semaphore semaphore0 =new Semaphore(0);


        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
    }
}
