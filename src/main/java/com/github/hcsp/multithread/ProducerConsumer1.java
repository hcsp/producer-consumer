package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Basket basket = new Basket();
        Object lock = new Object();

        Producer.activeCount();
        Producer producer = new Producer(basket, lock);
        Consumer consumer = new Consumer(basket, lock);


        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Basket basket;
        Object lock;

        public Producer(Basket basket, Object lock) {
            this.basket = basket;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (basket.getBasket().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    basket.setBasket(Optional.of(r));

                    lock.notify();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        Basket basket;
        Object lock;

        public Consumer(Basket basket, Object lock) {
            this.basket = basket;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!basket.getBasket().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer value = this.basket.getBasket().get();
                    basket.setBasket(Optional.empty());
                    System.out.println("consuming " + value);

                    lock.notify();


                }
            }

        }
    }

    public static class Basket {
        Optional<Integer> basket = Optional.empty();

        public Optional<Integer> getBasket() {
            return basket;
        }

        public void setBasket(Optional<Integer> basket) {
            this.basket = basket;
        }
    }
}
