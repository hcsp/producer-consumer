package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

/***
 *  LockSupport
 *  @author gongxuanzhang
 **/
public class ProducerConsumer3 {

    static final Queue<Integer> QUEUE = new LinkedList<>();


    public static void main(String[] args) throws InterruptedException {
        // 此处线程之间需要保存互相的Thread对象 类似于spring的循环依赖
        Producer producer = new Producer();
        Consumer consumer = new Consumer().setNeedNotify(producer);
        producer.setNeedNotify(consumer);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }


    public static class Producer extends Thread {
        Thread needNotify;


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (!QUEUE.isEmpty()) {
                    LockSupport.park();
                }
                int random = new Random().nextInt();
                QUEUE.offer(random);
                System.out.println("Producing " + random);
                LockSupport.unpark(needNotify);
            }
        }

        public Producer setNeedNotify(Thread needNotify) {
            this.needNotify = needNotify;
            return this;
        }
    }

    public static class Consumer extends Thread {
        Thread needNotify;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (QUEUE.isEmpty()) {
                    LockSupport.park();
                }
                System.out.println("Consuming " + QUEUE.poll());
                LockSupport.unpark(needNotify);
            }
        }

        public Consumer setNeedNotify(Thread needNotify) {
            this.needNotify = needNotify;
            return this;
        }
    }

}

