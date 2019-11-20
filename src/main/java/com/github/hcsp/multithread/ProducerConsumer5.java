package com.github.hcsp.multithread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用{@link Exchanger} 实现生产消费者模型
 */
public class ProducerConsumer5 {
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

    /**
     * 代表一个线程需要操作的资源类，资源存放在一个{@link Exchanger}中。
     */
    private static class Resource {
        private Exchanger<Integer> exchanger = new Exchanger<>();

        private void produceResource(int num) throws InterruptedException {
            System.out.println("Producing " + num);
            exchanger.exchange(num); // 产生阻塞直到另一个线程调用exchanger方法
//            System.out.println("交换到的值：" + exchanger.exchange(num));
        }

        private void consumeResource() throws InterruptedException {
            System.out.println("Consuming " + exchanger.exchange(null));
        }
    }
}
