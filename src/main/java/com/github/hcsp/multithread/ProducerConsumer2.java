package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        // 创建一个空的篮子
        ProducerConsumer2.Container container = new ProducerConsumer2.Container(lock);

        // 传递篮子和锁对象
        ProducerConsumer2.Producer producer = new ProducerConsumer2.Producer(container, lock);
        ProducerConsumer2.Consumer consumer = new ProducerConsumer2.Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    // 一把锁两个条件
    public static class Container {
        private Condition notConsumedYet; //还没被消费
        private Condition notProducedYet; //还没被生产

        // 创建锁
        public Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public void setNotConsumedYet(Condition notConsumedYet) {
            this.notConsumedYet = notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public void setNotProducedYet(Condition notProducedYet) {
            this.notProducedYet = notProducedYet;
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
        Container container;
        ReentrantLock lock;

        public Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    // isPresent()可以判断当前存没有存对象，容器为空了才生产东西
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing" + r);
                    // Optional.of(r)返回一个Optional对象
                    container.setValue(Optional.of(r));
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
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    // 清空容器
                    container.setValue(Optional.empty());

                    System.out.println("Consuming" + value);

                    container.getNotConsumedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
