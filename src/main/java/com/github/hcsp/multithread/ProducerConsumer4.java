package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/***
 *  使用Semaphore信号量实现(一个变量即可)
 *  @author gongxuanzhang
 **/
public class ProducerConsumer4 {

    static Queue<Integer> queue = new LinkedList<>();

    static Semaphore semaphore = new Semaphore(1);


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
            int i = 0;
            while (true) {
                while (queue.isEmpty()) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int random = new Random().nextInt();
                    queue.offer(random);
                    System.out.println("Producing " + random);
                    i++;
                    if (i == 10) {
                        return;
                    }

                }
                semaphore.release();
            }

        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            int i = 0;
            while (true) {
                while (!queue.isEmpty()) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Consuming " + queue.poll());
                    i++;
                    if (i == 10) {
                        return;
                    }
                }
                semaphore.release();
            }
        }
    }
}
