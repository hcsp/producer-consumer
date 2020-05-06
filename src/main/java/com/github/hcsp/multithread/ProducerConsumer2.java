package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock/Condition
 *
 * @author kwer
 * @date 2020/5/6 22:59
 */
public class ProducerConsumer2 {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition empty = lock.newCondition();
    private static Condition full = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    /**
     * 容器
     */
    static class Container {
        Object value;
    }

    public static class Producer extends Thread {
        Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value != null) {
                        try {
                            empty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int j = new Random().nextInt();
                    System.out.println("Producing " + j);
                    container.value = j;
                    full.signal();
                } finally {
                    lock.unlock();
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
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value == null) {
                        try {
                            full.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    empty.signal();
                } finally {
                    lock.unlock();
                }

            }
        }
    }
}
