package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    //Blocking Queue: SynchronousQueue
    static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
    static final BlockingQueue<Integer> queueSinal = new ArrayBlockingQueue<>(1);

    public static void main(String[] args) throws InterruptedException {
        queueSinal.put(0); //一开始就可以生产了
        for (int i = 0; i < 1000; i++) {
            new Producer().start();
            new Consumer().start();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                queueSinal.take();
                int product = new Random().nextInt();
                System.out.println("Producing " + product);
                queue.put(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Consuming " + queue.take());
                queueSinal.put(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
