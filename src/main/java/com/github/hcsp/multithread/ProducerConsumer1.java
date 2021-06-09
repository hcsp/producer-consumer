package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

/**
 * 使用Obj.wait()&Obj.notify()实现生产消费者模型
 * 通过生产者、消费者构造函数共享数据和锁
 */
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
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
        Container container;
        final Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    // 生产者线程:如果共享数据存在就一直等待,直到被消费者消费后唤醒
                    while (container.getSharedData().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.setSharedData(Optional.of(random));
                    lock.notify();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        final Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                synchronized (lock) {
                    // 消费者线程:如果拿不到数据就一直等待，拿到后清空数据，唤醒消费者
                    while (!container.getSharedData().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = container.getSharedData().get();
                    System.out.println("Consuming " + random);
                    container.setSharedData(Optional.empty());
                    lock.notify();
                }
            }
        }
    }

    public static class Container {

        private Optional<Integer> sharedData = Optional.empty();

        public Optional<Integer> getSharedData() {
            return sharedData;
        }

        public void setSharedData(Optional<Integer> sharedData) {
            this.sharedData = sharedData;
        }
    }
}
