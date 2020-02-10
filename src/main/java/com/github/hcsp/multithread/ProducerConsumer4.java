package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        SharedData shared = new SharedData();

        Thread producer = new Thread(new Producer(shared), "producer");
        Thread consumer = new Thread(new Consumer(shared), "consumer");

        consumer.start();
        producer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private SharedData shared;

        Producer(SharedData shared) {
            this.shared = shared;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int data = new Random().nextInt();
                    shared.produce(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private SharedData shared;

        Consumer(SharedData shared) {
            this.shared = shared;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int data = shared.consume();
                    //use data
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class SharedData {
        int data = 0;
        Semaphore semproducer = new Semaphore(1);
        //set initial count to 0, so a consumer needs to wait until data is available
        Semaphore semconsumer = new Semaphore(0);

        int consume() throws InterruptedException {
            try {
                semconsumer.acquire();
                System.out.println("Consumed:" + data);
                Thread.sleep(10);
                return data;
            } catch (InterruptedException e) {
                throw e;
            } finally {
                semproducer.release();
            }
        }

        void produce(int data) throws InterruptedException {
            try {
                semproducer.acquire();
                this.data = data;
                System.out.println("Produced:" + data);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semconsumer.release();
            }
        }
    }
}
