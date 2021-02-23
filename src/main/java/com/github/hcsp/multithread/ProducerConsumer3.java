package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static final int POISON = Integer.MAX_VALUE;


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(queue, 10);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private BlockingQueue<Integer> queue;
        private int max;

        public Producer(BlockingQueue<Integer> queue, int max) {
            this.queue = queue;
            this.max = max;
        }

        @Override
        public void run() {
            Random r = new Random();
            for (int i = 0; i < max; i++) {
                int rInt = r.nextInt();
                try {
                    while (true) {
                        if (queue.peek() == null) {
                            queue.put(rInt);
                            System.out.println("Producing " + rInt);
                            break;
                        } else {
                            sleep(100);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                queue.put(POISON);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static class Consumer extends Thread {
        private BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Integer val = queue.take();
                    if (val.equals(POISON)) {
                        break;
                    }
                    System.out.println("Consuming " + val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
