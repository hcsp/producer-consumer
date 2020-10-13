package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/***
 * wait notify实现
 * @author gongxuanzhang
 * */
public class ProducerConsumer1 {
    static final Queue<Integer> QUEUE = new LinkedList<>();

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
            synchronized (QUEUE) {
                for (int i = 0; i < 10; i++) {
                    while (!QUEUE.isEmpty()) {
                        try {
                            QUEUE.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = new Random().nextInt();
                    QUEUE.offer(random);
                    System.out.println("Producing " + random);
                    QUEUE.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (QUEUE) {
                    while (QUEUE.isEmpty()) {
                        try {
                            QUEUE.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + QUEUE.poll());
                    QUEUE.notify();
                }
            }
        }
    }
}
