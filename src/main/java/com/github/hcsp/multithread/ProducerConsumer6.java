package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/***
 *  使用闭锁实现
 *  @author gongxuanzhang
 **/
public class ProducerConsumer6 {

    static Queue<Integer> queue = new LinkedList<>();

    static CountDownLatch[] producerCount = new CountDownLatch[10];

    static CountDownLatch[] consumerCount = new CountDownLatch[10];

    static {
        for (int i = 0; i < 10; i++) {
            producerCount[i] = new CountDownLatch(1);
            consumerCount[i] = new CountDownLatch(1);
        }
        producerCount[0] = new CountDownLatch(0);
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
            for (int i = 0; i < 10; i++) {
                try {
                    producerCount[i].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int random = new Random().nextInt();
                queue.offer(random);
                System.out.println("Producing " + random);
                consumerCount[i].countDown();
            }

        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 9; i++) {
                    consumerCount[i].await();
                    System.out.println("Consuming " + queue.poll());
                    producerCount[i+1].countDown();
                }
                consumerCount[9].await();
                System.out.println("Consuming " + queue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
