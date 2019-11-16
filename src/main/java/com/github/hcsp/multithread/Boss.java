package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 1、Object.wait()/notify()/notifyAll()
 * 2、Lock/Condition
 * 3、BlockingQueue
 * 4、......
 */
public class Boss {
    private static final int CAPACITY = 1;
    private static BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(CAPACITY);
    private static BlockingQueue<Integer> signalQueue = new ArrayBlockingQueue<>(CAPACITY);

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer(blockingQueue, signalQueue);
        Consumer consumer = new Consumer(blockingQueue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class Producer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        private BlockingQueue<Integer> signalQueue;

        Producer(BlockingQueue<Integer> signalQueue, BlockingQueue<Integer> blockingQueue) {
            this.signalQueue = signalQueue;
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer value = new Random().nextInt();
                    blockingQueue.put(value);
                    System.out.println("Producing " + value);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        private BlockingQueue<Integer> signalQueue;

        Consumer(BlockingQueue<Integer> signalQueue, BlockingQueue<Integer> blockingQueue) {
            this.signalQueue = signalQueue;
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consumer " + blockingQueue.take());
                    signalQueue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 请实现一个生产者/消费者模型，其中：
    // 生产者生产10个随机的整数供消费者使用（随机数可以通过new Random().nextInt()获得）
    // 使得标准输出依次输出它们，例如：
    // Producing 42
    // Consuming 42
    // Producing -1
    // Consuming -1
    // ...
    // Producing 10086
    // Consuming 10086
    // Producing -12345678
    // Consuming -12345678
}
