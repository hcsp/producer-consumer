package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *  使用标志位  自旋实现
 *  @author gongxuanzhang
 **/
public class ProducerConsumer10 {


    static Queue<Integer> queue = new LinkedList<>();

    static AtomicBoolean atomicBoolean = new AtomicBoolean(true);

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
            for (int i = 0; i < 10; i++) {
                while (true) {
                    if (atomicBoolean.get()) {
                        int random = new Random().nextInt();
                        queue.offer(random);
                        System.out.println("Producing " + random);
                        atomicBoolean.set(false);
                        break;
                    }
                }
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (true) {
                    if (!atomicBoolean.get()) {
                        System.out.println("Consuming " + queue.poll());
                        atomicBoolean.set(true);
                        break;
                    }
                }
            }
        }
    }
}
