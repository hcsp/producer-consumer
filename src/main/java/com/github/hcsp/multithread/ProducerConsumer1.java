package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用原生的 'synchronized' 'wait/notify'机制完成生产者消费
 */
public class ProducerConsumer1 {
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

        public Producer(Resource resource) {
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

        public Consumer(Resource resource) {
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

        private synchronized void produceResource(int num) throws InterruptedException {
            while (!queue.isEmpty()) {
                this.wait();
            }
            System.out.println("Producing " + num);
            queue.add(num);
            this.notifyAll();
        }

        private synchronized void consumeResource() throws InterruptedException {
            while (queue.isEmpty()) {
                this.wait();
            }
            System.out.println("Consuming " + queue.remove());
            this.notifyAll();
        }
    }
}
