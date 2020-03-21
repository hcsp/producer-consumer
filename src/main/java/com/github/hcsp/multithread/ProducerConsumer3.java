package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {

        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(1);
        LinkedBlockingQueue<Object> signalQueue = new LinkedBlockingQueue<>(1);
        ProducerConsumer3.Producer producer = new ProducerConsumer3.Producer(queue, signalQueue);
        ProducerConsumer3.Consumer consumer = new ProducerConsumer3.Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        LinkedBlockingQueue queue;
        LinkedBlockingQueue signalQueue;

        public Producer(LinkedBlockingQueue queue, LinkedBlockingQueue signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int randomNumber = new Random().nextInt();
                System.out.println("Producing " + randomNumber);
                try {
                    queue.put(randomNumber);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        LinkedBlockingQueue queue;
        LinkedBlockingQueue signalQueue;

        public Consumer(LinkedBlockingQueue queue, LinkedBlockingQueue signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
