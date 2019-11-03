package com.github.hcsp.multithread;

<<<<<<< HEAD
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock/Condition 实现
 *
 * @author kelvin chen
 */
=======
>>>>>>> fc56ff0... - 修改Boss.java中的join笔误
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

<<<<<<< HEAD
        final Lock lock = new ReentrantLock();
        final Condition emptyCondition = lock.newCondition();
        final Condition fullCondition = lock.newCondition();

        Stack<Integer> ret = new Stack<>();

        Producer producer = new Producer(ret, lock, emptyCondition, fullCondition);
        Consumer consumer = new Consumer(ret, lock, emptyCondition, fullCondition);
=======
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
>>>>>>> fc56ff0... - 修改Boss.java中的join笔误

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
