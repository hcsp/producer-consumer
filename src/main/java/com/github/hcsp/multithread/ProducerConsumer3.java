package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 利用java提供的阻塞式同步容器来实现生产者消费者模型
 * BlockingQueue
 */
public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    static BlockingQueue<Integer> lists = new LinkedBlockingQueue<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                try {
                    lists.put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producing " + r);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int r = lists.take();
                    System.out.println("Consuming " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
