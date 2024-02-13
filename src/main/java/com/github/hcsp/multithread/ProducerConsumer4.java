package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        Container container = new Container();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Producer producer = new Producer(container, semaphore);
        Consumer consumer = new Consumer(container, semaphore);

        for (int i = 0; i < 10; i++) {
            threadPool.execute(producer);
            threadPool.execute(consumer);
        }
        threadPool.shutdown();
    }

    public static class Producer extends Thread {
        Container container;
        Semaphore semaphore;


        public Producer(Container container, Semaphore semaphore) {
            this.container = container;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                while (!container.getValue().isPresent()) {
                    semaphore.acquire();
                    int r = new Random().nextInt();
                    container.setValue(Optional.of(r));
                    System.out.println("Producing " + r);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Semaphore semaphore;

        public Consumer(Container container, Semaphore semaphore) {
            this.container = container;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                while (container.getValue().isPresent()) {
                    semaphore.acquire();
                    Integer value = container.getValue().get();
                    System.out.println("Consumer:" + value);
                    container.setValue(Optional.empty());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
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
