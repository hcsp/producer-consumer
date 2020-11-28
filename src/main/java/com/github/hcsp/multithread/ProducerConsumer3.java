package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) {
        BlockingDeque<Integer> integerQueue = new LinkedBlockingDeque<>(1);
        // BlockingDeque<Integer> signal = new LinkedBlockingDeque<>(1);
        new Thread(() -> threadProduce(integerQueue), "Producing").start();
        new Thread(() -> threadConsume(integerQueue), "Consuming").start();
    }

    static void threadProduce(BlockingDeque<Integer> integerQueue) {
        for (int i = 0; i < 10; i++) {
            int temp = new Random().nextInt(100);
            try {
                integerQueue.put(temp);
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void threadConsume(BlockingDeque<Integer> integerQueue) {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        integerQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
