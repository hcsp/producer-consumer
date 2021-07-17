package com.github.hcsp.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final Lock lock = new ReentrantLock();
    private static final Condition notProducedYet = lock.newCondition();
    private static final Condition notConsumedYet = lock.newCondition();

    private static final Basket basket = new Basket();
    private static int index = 0;

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
            new CreateRunInstance().run(Type.PRODUCE);
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            new CreateRunInstance().run(Type.CONSUME);
        }
    }

    private static class CreateRunInstance implements CreateRun {
        @Override
        public void run(Enum<Type> type) {
            lock.lock();
            try {
                while (index < 10) {
                    if (type == Type.CONSUME) {
                        consume();
                    } else {
                        produce();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void produce() throws InterruptedException {
            while (basket.getValue().isPresent()) {
                notProducedYet.await();
            }

            Worker.Produce(basket);
            notConsumedYet.signal();
        }

        @Override
        public void consume() throws InterruptedException {
            while (!basket.getValue().isPresent()) {
                notConsumedYet.await();
            }
            Worker.Consume(basket);
            index++;
            notProducedYet.signal();
        }
    }
}
