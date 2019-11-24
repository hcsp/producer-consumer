package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class Boss {
    public static void main(String[] args) throws InterruptedException {
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
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingDeque<>();
        Semaphore emptyState = new Semaphore(1);
        Semaphore fullState = new Semaphore(0);

        Producer producer = new Producer(sharedQueue, emptyState, fullState);
        Consumer consumer = new Consumer(sharedQueue, emptyState, fullState);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }
}
