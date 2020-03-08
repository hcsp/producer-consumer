package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer4 {
    private static boolean isProduced = false;

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Producer producer = new Producer(lock, condition);
        Consumer consumer = new Consumer(lock, condition);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Shared {
        static int sharedNum = 0;

        static void produceNum() {
            sharedNum = new Random().nextInt();
            System.out.println("Producing " + sharedNum);
            isProduced = true;
        }

        static void consumeNum() {
            System.out.println("Consuming " + sharedNum);
            isProduced = false;
        }
    }

    public static class Producer extends Thread {
        ReentrantLock lock;
        Condition condition;

        Producer(ReentrantLock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (isProduced) {
                        condition.await();
                    }
                    Shared.produceNum();
                    condition.signalAll();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock;
        Condition condition;

        Consumer(ReentrantLock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (!isProduced) {
                        condition.await();
                    }
                    Shared.consumeNum();
                    condition.signalAll();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
