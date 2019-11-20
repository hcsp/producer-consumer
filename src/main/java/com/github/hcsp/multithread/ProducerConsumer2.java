package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用{@link Lock} ,{@link Condition}完成生产消费者模型
 */
public class ProducerConsumer2 {
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
        private Resource resource;

        Producer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    resource.produceResource(ThreadLocalRandom.current().nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Resource resource;

        Consumer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    resource.consumeResource();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 代表一个线程需要操作的资源类，资源存放在队列中
     */
    private static class Resource {
        private Queue<Integer> queue = new LinkedList<>();

        // ---------- lock ---------------
        private Lock lock = new ReentrantLock();
        private Condition canProduce = lock.newCondition();
        private Condition canConsume = lock.newCondition();

        private void produceResource(int num) throws InterruptedException {
            lock.lock();
            try {
                while (!queue.isEmpty()) {
                    canProduce.await();
                }
                System.out.println("Producing " + num);
                queue.add(num);
                canConsume.signalAll();
            } finally {
                lock.unlock();
            }
        }

        private void consumeResource() throws InterruptedException {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    canConsume.await();
                }
                System.out.println("Consuming " + queue.remove());
                canProduce.signalAll();
            } finally {
                lock.unlock();
            }

        }
    }
}
