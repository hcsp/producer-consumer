package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static final ReentrantLock lock = new ReentrantLock();
    public static Container container = new Container(lock);
    public static Condition notProducedYet = container.getNotProducedYet();
    public static Condition notConsumedYet = container.getNotConsumedYet();
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
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        notConsumedYet.await();
                    }
                    int value = new Random().nextInt();
                    container.setValue(Optional.of(value));
                    System.out.println("Producing " + value);
                    notProducedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        notProducedYet.await();
                    }
                    Integer value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);
                    notConsumedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
