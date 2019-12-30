package com.github.hcsp.multithread;

import java.util.Optional;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Optional<Integer> value = Optional.empty();
        Object lock = new Object();

        Producer producer = new Producer(value, lock);
        Consumer consumer = new Consumer(value, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Optional<Integer> value;
        Object lock;


        public Producer(Optional<Integer> value, Object lock) {
            this.value = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (value.isPresent()) {
                    lock.wait();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Optional<Integer> value;
        Object lock;

        public Consumer(Optional<Integer> value, Object lock) {
            this.value = value;
            this.lock = lock;
        }

        @Override
        public void run() {}
    }
}
