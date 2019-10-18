package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

        // the lock object
        final Lock lock = new ReentrantLock();
        final Condition empty = lock.newCondition();
        final Condition full = lock.newCondition();
        // the data container
        LinkedList<Integer> buffer = new LinkedList<>();
        Consumer producer = new Consumer(lock, buffer, full, empty);
        Producer consumer = new Producer(lock, buffer, full, empty);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }
}
