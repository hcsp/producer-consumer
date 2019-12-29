package com.github.hcsp.multithread;

import java.awt.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static class Container {
        private Container 还没有被消费掉;
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
        Container container = new Container();
        Object lock = new Object();


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
        final Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                // isPresent 判断当前存没存对象
                while (container.getValue().isPresent()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing" + r);
                container.setValue(Optional.of(r));

                lock.notify();

            }
        }
    }

    public static class Consumer extends Thread {
        // 初始为空
        Container container;
        Object lock;

        public Consumer(Container value, Object lock) {
            this.container = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (container.getValue().isPresent()) {
                    lock.wait();
                }

                Integer value = container.getValue().get();
                container.setValue();
                System.out.println("Consuming" + value);

                lock.notify();
            }
        }
    }
}
