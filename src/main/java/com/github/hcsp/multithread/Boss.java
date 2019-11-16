package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;

public class Boss {

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < 1; i++) {
            new Thread(new Producer(queue)).start();
            new Thread(new Consumer(queue)).start();
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
