package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {

        //创建一个容器
        Container container = new Container();
        container.setValue(Optional.empty());
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
        Object lock;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    //对于生产者，如果value里有东西，就要等待
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //当value为空时，生产则生产东西
                    //Random r = new Random();
                    int r = (new Random()).nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));

                    //唤醒消费者
                    lock.notify();

                }
            }

        }

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }
    }

    public static class Consumer extends Thread {
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
                    //如果不存在，就一直等待
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //以下是消费的代码
                    Integer value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);

                    lock.notify();

                }
            }

        }
    }


    public static class Container {
        private Optional<Integer> value;

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
