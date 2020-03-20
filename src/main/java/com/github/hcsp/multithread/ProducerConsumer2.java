package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Box box = new Box(lock);
        Producer producer = new Producer(lock, box);
        Consumer consumer = new Consumer(lock, box);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Box {
        Condition 未被消费;
        Condition 未被生产;

        private Box(ReentrantLock lock) {
            this.未被消费 = lock.newCondition();
            this.未被生产 = lock.newCondition();
        }

        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        ReentrantLock lock;
        Box box;

        public Producer(ReentrantLock lock, Box box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {


                try {
                    lock.lock();
                    while (box.getValue().isPresent()) {
                        try {
                            box.未被消费.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    box.setValue(Optional.of(r));
                    box.未被生产.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock;
        Box box;

        public Consumer(ReentrantLock lock, Box box) {
            this.lock = lock;
            this.box = box;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {


                try {
                    lock.lock();
                    while (!box.getValue().isPresent()) {
                        try {
                            box.未被生产.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + box.getValue().get());
                    box.setValue(Optional.empty());
                    box.未被消费.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
