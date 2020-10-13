package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/***
 *  使用栅栏实现
 *  需要用到自旋 不依赖其他类和方法的情况下还没想到好的解决办法
 *  @author gongxuanzhang
 **/
public class ProducerConsumer7 {

    static Queue<Integer> queue = new LinkedList<>();

    static CyclicBarrier[] cyclicBarriers = new CyclicBarrier[11];

    static {
        for (int i = 0; i < 11; i++) {
            cyclicBarriers[i] = new CyclicBarrier(2);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }

    public static class Producer extends Thread {


        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    while (!queue.isEmpty()) {
                    }
                    int random = new Random().nextInt();
                    queue.offer(random);
                    System.out.println("Producing " + random);
                    cyclicBarriers[0].await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (queue.isEmpty()) {
                }
                System.out.println("Consuming " + queue.poll());
                try {
                    cyclicBarriers[0].await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
