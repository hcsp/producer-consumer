package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Object lock = new Object();

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        final Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getShareData().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.setShareData(Optional.of(random));
                    lock.notify();
                }
            }
        }
    }


    public static class Consumer extends Thread {
        Container container;
        final Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getShareData().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getShareData().get());
                    container.setShareData(Optional.empty());
                    lock.notify();
                }
            }
        }
    }

    public static class Container {

        private Optional<Integer> shareData = Optional.empty();

        public Optional<Integer> getShareData() {
            return shareData;
        }

        public void setShareData(Optional<Integer> shareData) {
            this.shareData = shareData;
        }
    }
}

