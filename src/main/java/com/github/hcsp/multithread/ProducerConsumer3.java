package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumer3 {

    private static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);


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
            int nextInt = new Random().nextInt();
            try {
                queue.put(nextInt);
                System.out.println("Producing " + nextInt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                Integer pro = queue.take();
                System.out.println("Consuming " + pro);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
