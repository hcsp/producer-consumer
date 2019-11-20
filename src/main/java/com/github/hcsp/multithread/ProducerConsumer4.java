package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource();

        Producer producer = new Producer(resource);
        Consumer consumer = new Consumer(resource);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        Resource resource;

        public Producer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    resource.produceResource(ThreadLocalRandom.current().nextInt());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        private Resource resource;

        public Consumer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    resource.consumeResource();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Resource {
        private Queue<Integer> queue = new LinkedList<>();

        // =========== Semaphore ============
        private Semaphore producerSemaphore = new Semaphore(1);
        // 设置为0是保证消费者一定是在生产者生产之后才进行消费
        private Semaphore consumerSemaphore = new Semaphore(0);

        // ------------ method -----------
        private void produceResource(int num) throws InterruptedException {
            producerSemaphore.acquire(); // 拿到生产权限
            synchronized (this) {
                System.out.println("Producing " + num);
                queue.add(num);
            }
            consumerSemaphore.release(); // 释放消费权限
        }

        private void consumeResource() throws InterruptedException {
            consumerSemaphore.acquire();
            synchronized (this) {
                System.out.println("Consuming " + queue.remove());
            }
            producerSemaphore.release();
        }
    }
}
