package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Container container = new Container(lock);

        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        ReentrantLock lock;
        Container container;

        public Producer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    //盘子里有东西
                    while (container.getValue().isPresent()) {
                        container.getHaveAlreadyConsumed().await();
                    }

                    int r = new Random().nextInt();
                    container.setValue(Optional.of(r));
                    System.out.println("Producing " + r);
                    container.getHaveAlreadyProduced().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }


        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock;
        Container container;

        public Consumer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    //盘子里是空的，不满足已经生产出来的条件，所以Consumer进程等待
                    while (!container.getValue().isPresent()) {
                        container.getHaveAlreadyProduced().await();
                    }

                    //盘子里有东西了
                    Integer integer = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + integer);
                    //盘子里又空了，满足“我已经吃完了”的条件，唤醒Consumer进程
                    container.getHaveAlreadyConsumed().signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        }
    }

    public static class Container {
        private Condition haveAlreadyConsumed;
        private Condition haveAlreadyProduced;
        Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        public Container(ReentrantLock lock) {
            this.haveAlreadyConsumed = lock.newCondition();
            this.haveAlreadyProduced = lock.newCondition();
        }

        public Condition getHaveAlreadyConsumed() {
            return haveAlreadyConsumed;
        }

        public Condition getHaveAlreadyProduced() {
            return haveAlreadyProduced;
        }
    }
}
