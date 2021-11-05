package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {


        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {

        BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        BlockingQueue<Integer> signalQueue;


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    queue.put(r);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        BlockingQueue<Integer> signalQueue;


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    // 随便放一个
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ;
            }
        }
    }

    public static class Container {
        private Condition notConsumedYet; // 还没有被消费掉
        private Condition notProducedYet; // 还没有被生产出来

        public Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        private Optional<Integer> value = Optional.empty();


        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

}
