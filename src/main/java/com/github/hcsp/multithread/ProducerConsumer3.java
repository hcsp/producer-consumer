package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingQueue
 */
public class ProducerConsumer3 {

    static final BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
    static CyclicBarrier barrier = new CyclicBarrier(2);

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                int num = new Random().nextInt();
                System.out.println("Producing " + num);
                blockingQueue.add(num);

                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                try {
                    Integer take = blockingQueue.take();
                    System.out.println("Consuming " + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
