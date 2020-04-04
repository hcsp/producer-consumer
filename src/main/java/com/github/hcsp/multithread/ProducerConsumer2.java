package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();
        MyBox box = new MyBox(lock);

        Producer producer = new Producer(lock, box);
        Consumer consumer = new Consumer(lock, box);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class MyBox extends Box<Integer> {
        final Condition notEmpty;
        final Condition notFull;

        public MyBox(ReentrantLock lock) {
            this.notEmpty = lock.newCondition();
            this.notFull = lock.newCondition();
        }
    }

    public static class Producer extends Thread {

        private final ReentrantLock lock;
        private final MyBox box;

        public Producer(ReentrantLock lock, MyBox box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!box.isEmpty()) {
                        box.notEmpty.await();
                    }

                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    box.put(value);
                    box.notFull.signal();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        private final ReentrantLock lock;
        private final MyBox box;

        public Consumer(ReentrantLock lock, MyBox box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (box.isEmpty()) {
                        box.notFull.await();
                    }

                    System.out.println("Consuming " + box.take());
                    box.notEmpty.signal();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
