package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingDeque<Integer> queen = new LinkedBlockingDeque<>(1);
        BlockingDeque<Integer> signalQueen = new LinkedBlockingDeque<>(1);
        Producer producer = new Producer(queen, signalQueen);
        Consumer consumer = new Consumer(queen, signalQueen);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingDeque<Integer> queen;
        BlockingDeque<Integer> signalQueue;

        public Producer(BlockingDeque<Integer> queen, BlockingDeque signalQueue) {
            this.queen = queen;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                try {
                    queen.put(r);
                    System.out.println("Producing " + r);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    public static class Consumer extends Thread {
        BlockingDeque<Integer> queen;
        BlockingDeque<Integer> signalQueue;

        public Consumer(BlockingDeque<Integer> queen, BlockingDeque signalQueue) {
            this.queen = queen;
            this.signalQueue = signalQueue;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    signalQueue.put(0);
                    System.out.println("Consuming " + queen.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
