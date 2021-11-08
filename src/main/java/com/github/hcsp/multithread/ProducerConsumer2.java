package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition isProduce = reentrantLock.newCondition();
        Condition isConsume = reentrantLock.newCondition();

        Producer producer = new Producer(container, reentrantLock, isProduce, isConsume);
        Consumer consumer = new Consumer(container, reentrantLock, isProduce, isConsume);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;
        private final ReentrantLock reentrantLock;
        private final Condition isProduce;
        private final Condition isConsume;

        public Producer(Container container, ReentrantLock reentrantLock, Condition isProduce, Condition isConsume) {
            this.container = container;
            this.reentrantLock = reentrantLock;
            this.isProduce = isProduce;
            this.isConsume = isConsume;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                if (container.getValue().isPresent()) {
                    try {
                        isProduce.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.setValue(Optional.of(r));
                isConsume.signal();
                reentrantLock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final ReentrantLock reentrantLock;
        private final Condition isProduce;
        private final Condition isConsume;

        public Consumer(Container container, ReentrantLock reentrantLock, Condition isProduce, Condition isConsume) {
            this.container = container;
            this.reentrantLock = reentrantLock;
            this.isProduce = isProduce;
            this.isConsume = isConsume;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                if (!container.getValue().isPresent()) {
                    try {
                        isConsume.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Consuming " + container.getValue().get());
                container.setValue(Optional.empty());
                isProduce.signal();
                reentrantLock.unlock();
            }
        }
    }

    private static class Container {
        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        private Optional<Integer> value = Optional.empty();
    }
}
