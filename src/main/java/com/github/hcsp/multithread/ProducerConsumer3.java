package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>(1);
        final Integer CYCLE_INDEX = 10;

        Producer producer = new Producer(queue, signalQueue, CYCLE_INDEX);
        Consumer consumer = new Consumer(queue, signalQueue, CYCLE_INDEX);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final BlockingQueue<Integer> queue;
        private final Integer cycleIndex;
        private final BlockingQueue<Integer> signalQueue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue, Integer CYCLE_INDEX) {
            this.queue = queue;
            this.cycleIndex = CYCLE_INDEX;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                Integer value = new Random().nextInt();
                System.out.println("Producing " + value);
                try {
                    queue.put(value);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final BlockingQueue<Integer> queue;
        private final Integer cycleIndex;
        private final BlockingQueue<Integer> signalQueue;
        private static final Integer WAKE_SIGNAL = 0;

        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue, Integer CYCLE_INDEX) {
            this.queue = queue;
            this.cycleIndex = CYCLE_INDEX;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                try {
                    Integer value = queue.take();
                    System.out.println("Consuming " + value);
                    signalQueue.put(WAKE_SIGNAL);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
