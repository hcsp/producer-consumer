package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container(new LinkedBlockingQueue());
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();


    }

    public static class Producer extends Thread {
        private Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (container) {
                    while (0 == container.queue.size()) {
                        try {
                            int num = new Random().nextInt();
                            container.queue.put(num);
                            System.out.println("Producing " + num);
                            container.notify();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Producer(Container container) {
            this.container = container;
        }
    }


    public static class Consumer extends Thread {
        private Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (container) {
                    while (0 != container.queue.size()) {
                        try {
                            Integer num = container.queue.poll();
                            System.out.println("Consuming " + num);
                            container.notify();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Consumer(Container container) {
            this.container = container;
        }
    }

    public static class Container extends Thread {
        private BlockingQueue<Integer> queue;

        public Container(BlockingQueue queue) {
            this.queue = queue;
        }
    }
}
