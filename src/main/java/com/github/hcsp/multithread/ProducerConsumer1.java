package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        // synchronized (lock)/wait(),notify() 方式 生产者producer 消费者 consumer 容器container  生产者看到容器为空则生产，消费者看到容器不为空，就消费
        Object lock = new Object();
        Container container = new Container();

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();  //等待该线程终止。
        consumer.join();
    }


    static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    static class Producer extends Thread {
        Container container;
        Object lock;

        Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getValue().isPresent()) {   //容器有值，则等待；否则，则开始生产
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.setValue(Optional.of(random));
                    lock.notify();
                }
            }
        }
    }

    static class Consumer extends Thread {
        Container container;
        Object lock;


        Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getValue().isPresent()) {  //容器为空，则等待；否则，则消费
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int randomGet = container.getValue().get();
                    container.setValue(Optional.empty());  //消费完之后将盘子清空
                    System.out.println("Consuming " + randomGet);
                    lock.notify();
                }
            }
        }

    }
}
