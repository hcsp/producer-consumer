package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1,true);
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
            for (int i = 0; i < 10; i++) {
                int num = new Random().nextInt();
                try {
                    queue.put(num);
                    System.out.println("Producing " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer num = null;
                try {
                    num = queue.take();
                    System.out.println("Consuming " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
