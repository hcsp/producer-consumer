package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 信号量
 *
 * @author kwer
 * @date 2020/5/6 23:13
 */
public class ProducerConsumer4 {

    private static Semaphore emptySemaphore = new Semaphore(1);
    private static Semaphore fullSemaphore = new Semaphore(0);

    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();


    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    emptySemaphore.acquire();
                    synchronized (ProducerConsumer4.class) {
                        int j = new Random().nextInt();
                        System.out.println("Producing " + j);
                        queue.add(j);
                    }
                    fullSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    fullSemaphore.acquire();
                    System.out.println("Consuming " + queue.remove());
                    emptySemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
