package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    //Exchanger
    static final Exchanger<Product> repository = new Exchanger<Product>();
    static Product product = new Product();

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
            try{
                int newProduct = new Random().nextInt();
                product.setProduct(newProduct);
                System.out.println("Producing " + newProduct);
                repository.exchange(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try{
                product = repository.exchange(product);
                System.out.println("Consuming " + product.getProduct());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Product {
        private Boolean isFull = Boolean.FALSE;
        private Boolean isEmpty = Boolean.TRUE;
        private int product;

        public Boolean isFull() {
            return isFull;
        }

        public void setFull(Boolean full) {
            isFull = full;
        }

        public Boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(Boolean empty) {
            isEmpty = empty;
        }

        public int getProduct() {
            return product;
        }

        public void setProduct(int product) {
            this.product = product;
        }
    }
}
