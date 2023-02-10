package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();

        Producer producer = new Producer(container, LOCK);
        Consumer consumer = new Consumer(container, LOCK);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Container {

        private Integer item;

        public Integer getItem() {
            return item;
        }

        public void setItem(Integer item) {
            this.item = item;
        }
    }

    public static class Producer extends Thread {

        private final Container container;
        private final Object LOCK;

        public Producer(Container container, Object LOCK) {
            this.container = container;
            this.LOCK = LOCK;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    if (container.getItem() != null) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int item = new Random().nextInt();
                    container.setItem(item);
                    System.out.println("Producing " + item);
                    LOCK.notify();
                }
            }
        }
    }


    public static class Consumer extends Thread {
        private final Container container;
        private final Object LOCK;

        public Consumer(Container container, Object LOCK) {
            this.container = container;
            this.LOCK = LOCK;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    if (container.getItem() == null) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 消费
                    System.out.println("Consuming " + container.getItem());
                    container.setItem(null);
                    LOCK.notify();
                }
            }
        }
    }
}
