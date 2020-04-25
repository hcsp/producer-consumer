package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* BlockingQueue */
public class ProducerConsumer3 {

    static BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<Integer>(10);

    public static class ValueObject {
        public static int value = 0;
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);

        for (int i = 0; i < 10; i++) {
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }

    public static class Producer extends Thread {

        BlockingQueue<Integer> blockingQueue;

        Producer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while (ValueObject.value == 0) {
                try {
                    ValueObject.value = new Random().nextInt();
                    blockingQueue.put(ValueObject.value);
                    System.out.println("Producing " + ValueObject.value);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        BlockingQueue<Integer> blockingQueue;

        Consumer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while (ValueObject.value != 0) {
                try {
                    ValueObject.value = blockingQueue.take();
                    System.out.println("Consuming " + ValueObject.value);
                    ValueObject.value = 0;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
