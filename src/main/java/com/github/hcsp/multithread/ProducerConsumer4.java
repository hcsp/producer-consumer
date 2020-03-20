package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(1);
        Semaphore lock = new Semaphore(1);
        Box box = new Box();
        Producer producer = new Producer(box, full, empty, lock);
        Consumer consumer = new Consumer(box, full, empty, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();


    }

    public static class Box {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        Box box;
        Semaphore full;
        Semaphore empty;
        Semaphore lock;

        public Producer(Box box, Semaphore full, Semaphore empty, Semaphore lock) {
            this.box = box;
            this.full = full;
            this.empty = empty;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {


                int r = new Random().nextInt();
                try {
                    empty.acquire();
                    lock.acquire();
                    System.out.println("Producing " + r);
                    box.setValue(Optional.of(r));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    full.release();
                    lock.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Box box;
        Semaphore full;
        Semaphore empty;
        Semaphore lock;

        public Consumer(Box box, Semaphore full, Semaphore empty, Semaphore lock) {
            this.box = box;
            this.full = full;
            this.empty = empty;
            this.lock = lock;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    full.acquire();
                    lock.acquire();
                    System.out.println("Consuming " + box.getValue().get());
                    box.setValue(Optional.empty());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    empty.release();
                    lock.release();
                }
            }
        }
    }
}
