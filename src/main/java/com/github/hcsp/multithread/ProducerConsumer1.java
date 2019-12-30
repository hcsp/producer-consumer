package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer1 {
    public static class Container {
        private Condition notConsumedYet; // 还没有被消费
        private Condition notProducedYet; // 还没有被生产
        // 任何构造器都要和锁绑定在一起
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

        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 初始为空 Optional 特殊的容器,可能存储,也可能没有
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);


        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }
    public static class Producer extends Thread {
        // 初始为空
        Container container;
        final ReentrantLock lock;

        public Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing" + r);
                    container.setValue(Optional.of(r));

                    container.getNotProducedYet().notify();
                } finally {
                    lock.unlock();
                }
//                synchronized (lock) {
                    // isPresent 判断当前存没存对象

//                }
            }
        }
    }

    public static class Consumer extends Thread {
        // 初始为空
        Container container;
        ReentrantLock lock;

        public Consumer(Container value, ReentrantLock lock) {
            this.container = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
//                synchronized (lock) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer value = container.getValue().get();
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
