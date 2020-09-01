package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ProducerConsumer5 {
   /* public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }*/

    public static class Producer extends Thread {
        @Override
        public void run() {
        }
    }


    public static class Consumer extends Thread {
        @Override
        public void run() {
        }
    }
}
