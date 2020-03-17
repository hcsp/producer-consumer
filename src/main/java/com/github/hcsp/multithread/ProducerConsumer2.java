package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);
        container.setValue(Optional.empty());

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        ReentrantLock lock;

        public Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();    //申请🔒的语句
                try {
                    //对于生产者，如果value里有东西，就要等待
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //当value为空时，生产则生产东西
                    int r = (new Random()).nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));

                    //唤醒消费者
                    container.getNotProducedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }


    }


    public static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;

        public Consumer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    //如果不存在，就一直等待
                    while (!container.getValue().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //以下是消费的代码
                    Integer value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);

                    container.getNotConsumedYet().signal();

                } finally {
                    lock.unlock();
                }
            }

        }
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;

        public Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        private Optional<Integer> value;

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
