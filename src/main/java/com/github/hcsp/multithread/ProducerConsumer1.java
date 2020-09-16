package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {

    static Integer num;

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                synchronized (ProducerConsumer1.class) {
                    if (count == 10) {
                        break;
                    }
                    if (num == null) {
                        num = new Random().nextInt();
                        System.out.println("Producing " + num);
                        count++;
                        ProducerConsumer1.class.notify();
                    } else {
                        try {
                            ProducerConsumer1.class.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            int temp;
            int count = 0;
            while (true) {
                synchronized (ProducerConsumer1.class) {
                    if (count == 10) {
                        break;
                    }
                    if (num == null) {
                        try {
                            ProducerConsumer1.class.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    } else {
                        temp = num;
                        num = null;
                        count++;
                        System.out.println("Consuming " + temp);
                        ProducerConsumer1.class.notify();
                    }
                }
            }
        }
    }
}
