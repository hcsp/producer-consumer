package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个空的篮子
        Container container = new Container();
        Object lock = new Object();

        // 传递篮子和锁对象
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        // 此时用的传入的对象，是用的同一个对象
        Container container;
        Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    // isPresent()可以判断当前存没有存对象，容器为空了才生产东西
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    // Optional.of(r)返回一个Optional对象
                    container.setValue(Optional.of(r));
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        // 此时用的传入的对象，是用的同一个对象
        Container container;
        Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    // 清空容器
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);

                    lock.notify();
                }
            }
        }
    }
}
