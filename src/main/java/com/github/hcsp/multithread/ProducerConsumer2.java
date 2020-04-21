package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock/condition完成生产者消费者模型
 */
public class ProducerConsumer2 {
    static Container container = new Container();
    static Lock lock = new ReentrantLock();
    static Condition emptyCondition = lock.newCondition();
    static Condition fullCondition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Container {
        private Object value;

        public Container() {
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 5; i < 10; i++) {
                while (container.value != null) {
                    try {
                        lock.lock();
                        fullCondition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
                container.value = new Random().nextInt();
                System.out.println("Producing " + container.value);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 5; i < 10; i++) {
                while (container.value == null) {
                    try {
                        lock.lock();
                        emptyCondition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
                System.out.println("Consuming " + container.value);
                container.value = null;
            }
        }
    }
}
