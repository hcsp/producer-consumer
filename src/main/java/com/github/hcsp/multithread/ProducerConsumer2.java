package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        ProducerConsumer1.Container container = new ProducerConsumer1.Container();

        Producer producer = new Producer(lock, condition, container);
        Consumer consumer = new Consumer(lock, condition, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        final ReentrantLock lock;
        final Condition condition;
        final ProducerConsumer1.Container container;

        Producer(ReentrantLock lock, Condition condition, ProducerConsumer1.Container container) {
            this.lock = lock;
            this.container = container;
            this.condition = condition;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while (container.getContainer().isPresent()) {
                    condition.await();
                }
                Integer num = new Random().nextInt();
                System.out.println("Producing " + num);
                container.setContainer(Optional.of(num));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                condition.signal();
            }

        }
    }

    public static class Consumer extends Thread {
        final ReentrantLock lock;
        final Condition condition;
        final ProducerConsumer1.Container container;

        Consumer(ReentrantLock lock, Condition condition, ProducerConsumer1.Container container) {
            this.lock = lock;
            this.container = container;
            this.condition = condition;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while (!container.getContainer().isPresent()) {
                    condition.await();
                }
                System.out.println("Consuming " + container.getContainer().get());
                container.setContainer(Optional.empty());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                condition.signal();
            }
        }
    }
}
