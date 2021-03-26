package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition producerCondition = lock.newCondition();
    private static Condition consumerCondition = lock.newCondition();

    private static Integer pro;

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
            lock.lock();
            try {
                while (pro != null) {
                    producerCondition.await();
                }
                int nextInt = new Random().nextInt();
                pro = nextInt;
                System.out.println("Producing " + nextInt);
                consumerCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                while (pro == null) {
                    consumerCondition.await();
                }
                System.out.println("Consuming " + pro);
                pro = null;
                producerCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
