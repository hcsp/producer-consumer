package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    //Condition/lock(),unlock()
    public static void main(String[] args) throws InterruptedException {
        // Condition方式 生产者producer 消费者 consumer 容器container  生产者看到容器为空则生产，消费者看到容器不为空，就消费
        Object lock = new Object();
        ProducerConsumer1.Container container = new ProducerConsumer1.Container();

        ProducerConsumer1.Producer producer = new ProducerConsumer1.Producer(container, lock);
        ProducerConsumer1.Consumer consumer = new ProducerConsumer1.Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();  //等待该线程终止。
        consumer.join();
    }


    static class Container {

        private Condition notConsumedYet;
        private Condition notProducedYet;

        Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        //外部需要读取，所以给两个get
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

    static class Producer extends Thread {
        Container container;
        ReentrantLock lock;

        Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                try {

                    lock.lock(); //加锁

                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await(); //容器有值，则继续等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.setValue(Optional.of(random));

                    container.getNotProducedYet().signal(); //告知已生产


                } finally {
                    lock.unlock(); //解锁
                }
            }
        }
    }

    static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;


        Consumer(Container container, ReentrantLock lock) {
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
                            container.getNotProducedYet().await();  //
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int randomGet = container.getValue().get();
                    container.setValue(Optional.empty());  //消费完之后将盘子清空
                    System.out.println("Consuming " + randomGet);

                    container.getNotConsumedYet().signal(); //告知已消费

                } finally {
                    lock.unlock();
                }
            }
        }

    }
}
