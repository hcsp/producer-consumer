package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Boss {
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();
        Lock lock = new ReentrantLock();
        Condition queueEmpty = lock.newCondition();
        Condition queueFull = lock.newCondition();

        Thread producer = new Thread(new Producer2(queue, lock, queueEmpty, queueFull));
        Thread consumer = new Thread(new Consumer2(queue, lock, queueEmpty, queueFull));

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
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
