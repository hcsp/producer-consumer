package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用{@link BlockingQueue} 完成生产消费者模型
 */
public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource(1);

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
        /**
         * 存放资源的阻塞队列
         */
        private BlockingQueue<Integer> queue;

        Resource(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("队列容量不能小于零");
            }
            queue = new LinkedBlockingQueue<>(size);
        }

        private void produceResource(int num) throws InterruptedException {
            System.out.println("Producing " + num);
            queue.put(num);
        }

        private void consumeResource() throws InterruptedException {
            System.out.println("Consuming " + queue.take());
        }
    }
}
