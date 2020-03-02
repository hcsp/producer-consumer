package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        //BlockingQueue 并发容器 几乎是线程安全的，但是使用不当可能会死锁
        //take() 假如队列是空的，则等待，直到队列有值，则拿走第一个
        //put()  假如队列满了，则等待，直到队列有空位，再往里面放东西
//        Object lock = new Object();
//        ProducerConsumer1.Container container = new ProducerConsumer1.Container();
        BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(1);
        BlockingQueue<Integer> signalQueue = new LinkedBlockingDeque<>(1);

        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();  //等待该线程终止。
        consumer.join();
    }


    static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalQueue; //信号队列，用于通知对方是否已生产或已消费

        Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int random = new Random().nextInt();
                System.out.println("Producer " + random);
                try {
                    queue.put(random);  //put完之后让其等待，则  因为是空的肯定会等待
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer extends Thread {

        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalQueue;

        Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consume " + queue.take());  //take()会一直等待直到有东西则拿
                    signalQueue.put(0);   //只是信号queue传递信号，所以，随便放一个值
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
