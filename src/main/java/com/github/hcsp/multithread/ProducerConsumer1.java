package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {

    private static int MAX_NUM = 10;
    private static List<Integer> container = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (container) {
                    // 如果容器已满，停止生产,
                    while (container.size() == MAX_NUM) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 否则继续生产
                    int generatorNum = new Random().nextInt();
                    container.add(generatorNum);
                    System.out.println("Producing" + generatorNum);
                    //通知
                    container.notifyAll();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (container) {
                    // 如果容器为空，则等待通知
                    while (container.size() == 0) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 否则继续拿东西
                    int getNum = container.get(0);
                    container.remove(0);
                    System.out.println("Consuming" + getNum);
                    //通知
                    container.notifyAll();
                }
            }
        }
    }
}
