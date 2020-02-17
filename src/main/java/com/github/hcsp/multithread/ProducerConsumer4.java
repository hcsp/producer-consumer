package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static final Semaphore full= new Semaphore(0); //初始化0个产品
    static final Semaphore empty= new Semaphore(1); //初始化有1个空余位置放置产品
    static final Semaphore mutex = new Semaphore(1); //mutex 初始化每次最多只有一个线程可以读写

    public static int apple;  //盘子中存的数
    Exchanger a = new Exchanger<>();
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
            for (int i = 0; i < 10; i++) {
                try {
                    empty.acquire(); //等待空位
                    mutex.acquire(); //等待读写锁
                    apple = new Random().nextInt();
                    System.out.println("Producing " + apple);
                    mutex.release(); //释放读写锁
                    full.release(); //放置产品
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    full.acquire(); //等待产品
                    mutex.acquire(); //等待读写锁
                    System.out.println("Consuming " + apple);
                    mutex.release(); //释放读写锁
                    empty.release(); //释放空位
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


