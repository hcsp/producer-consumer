package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore product = new Semaphore(0);
        Semaphore consume = new Semaphore(0);
        List<Integer> shareObj = new ArrayList<>();

        Producer producer = new Producer(10, product, consume, shareObj);
        Consumer consumer = new Consumer(product, consume, shareObj);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private int max;

        private Semaphore product;

        private Semaphore consume;

        private List<Integer> shareObj;

        public Producer(int max, Semaphore product, Semaphore consume, List<Integer> shareObj) {
            this.max = max;
            this.product = product;
            this.consume = consume;
            this.shareObj = shareObj;
        }

        @Override
        public void run() {
            Random r = new Random();
            for (int i = 0; i < max; i++) {
                if (i == 0) {
                    doProduct(r);
                    continue;
                }
                try {
                    consume.acquire();
                    doProduct(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void doProduct(Random r) {
            int rInt = r.nextInt();
            shareObj.clear();
            shareObj.add(rInt);
            System.out.println("Producing " + rInt);
            product.release();
        }
    }

    public static class Consumer extends Thread {
        private Semaphore product;
        private Semaphore consume;
        private List<Integer> shareObj;

        public Consumer(Semaphore product, Semaphore consume, List<Integer> shareObj) {
            this.product = product;
            this.consume = consume;
            this.shareObj = shareObj;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (product.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                        System.out.println("Consuming " + shareObj.get(0));
                        consume.release();
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
