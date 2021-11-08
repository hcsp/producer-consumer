package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> single = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(blockingQueue, single);
        Consumer consumer = new Consumer(blockingQueue, single);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    public static class Producer extends Thread {
        BlockingQueue<Integer> container;
        BlockingQueue<Integer> single;

        public Producer(BlockingQueue<Integer> container, BlockingQueue<Integer> single) {
            this.container = container;
            this.single = single;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    container.put(r);
                    single.take(); // 等待Consumer通知我再生产
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> container;
        BlockingQueue<Integer> single;

        public Consumer(BlockingQueue<Integer> container, BlockingQueue<Integer> single) {
            this.container = container;
            this.single = single;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + container.take());
                    single.put(0); // 通知Producer再次生产
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
