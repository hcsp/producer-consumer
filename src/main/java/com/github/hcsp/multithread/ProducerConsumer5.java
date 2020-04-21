package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * 使用Exchanger完成生产者消费者模型
 */
public class ProducerConsumer5 {
    static Exchanger exchanger = new Exchanger();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    int value = new Random().nextInt();
                    exchanger.exchange(value);
                    System.out.println("Producing " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    int value = (int) exchanger.exchange(null);

                    //先生产再消费
                    Thread.sleep(10);

                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
