package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static Semaphore producer = new Semaphore(1);
    static Semaphore consumer = new Semaphore(0);
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
                if (count == 10) {
                    break;
                }
                try {
                    producer.acquire(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                num = new Random().nextInt();
                System.out.println("Producing " + num);
                count++;
                consumer.release();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                try {
                    consumer.acquire(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Consuming " + num);
                num = null;
                count++;
                producer.release();
            }
        }
    }
}
