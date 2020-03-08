package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    private static int randomNum;
    private static boolean isProduced = false;

    public static void main(String[] args) throws InterruptedException {
        final Object obj = new Object();
        Producer producer = new Producer(obj);
        Consumer consumer = new Consumer(obj);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Object obj;

        Producer(Object obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    produceRandomNum();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void produceRandomNum() throws InterruptedException {
            synchronized (obj) {
                if (isProduced) {
                    obj.wait();
                }
                randomNum = new Random().nextInt();
                System.out.println("Producing " + randomNum);
                isProduced = true;
                obj.notifyAll();
            }
        }
    }

    public static class Consumer extends Thread {
        private final Object obj;

        Consumer(Object obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    consumeRandomNum();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void consumeRandomNum() throws InterruptedException {
            synchronized (obj) {
                if (!isProduced) {
                    obj.wait();
                }
                isProduced = false;
                System.out.println("Consuming " + randomNum);
                obj.notifyAll();
            }
        }
    }
}
