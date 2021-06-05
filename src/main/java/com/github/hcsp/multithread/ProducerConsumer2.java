package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {


    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);
        new Thread(producer).start();
        Thread.sleep(100);
        new Thread(consumer).start();
    }

    public static class Producer implements Runnable {
        Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                container.put();
            }
        }

        public Producer(Container container) {
            this.container = container;
        }
    }

    public static class Consumer implements Runnable {
        Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                container.take();
            }
        }

        public Consumer(Container container) {
            this.container = container;
        }
    }

    public static class Container {
        private Integer value;
        private Lock lock;
        private Condition consumer;
        private Condition producer;

        public void put() {
            lock.lock();
            while (null == value) {
                int num = new Random().nextInt();
                value = num;
                System.out.println("Producing " + num);
                consumer.signalAll();
            }
            try {
                producer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void take()  {
            lock.lock();
            while (null != value) {
                Integer num = value;
                System.out.println("Consuming " + num);
                value = null;
                producer.signalAll();
            }
            try {
                consumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public Container() {
            lock = new ReentrantLock();
            consumer = lock.newCondition();
            producer = lock.newCondition();
        }
    }
}
