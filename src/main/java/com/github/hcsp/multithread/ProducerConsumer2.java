package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final Lock lock = new ReentrantLock();
    private static final Condition isConsumed = lock.newCondition();
    private static final Condition isProduced = lock.newCondition();

    private static List<Integer> basket = new ArrayList<>(1);
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
            if (basket.isEmpty()) {
                Worker.Produce(basket);
                isProduced.signal();
            } else {
                isConsumed.wait();
            }
        }

        @Override
        public void consume() throws InterruptedException {
            if (basket.isEmpty()) {
                isProduced.wait();
            } else {
                Worker.Consume(basket);
                index++;
                isConsumed.signal();
            }
        }
    }
}
