package com.github.hcsp.multithread;


import java.util.Random;
import java.util.concurrent.Exchanger;

/***
 *  使用Exchanger
 *  @author gongxuanzhang
 **/
public class ProducerConsumer8 {

    static Exchanger<Integer> exchanger = new Exchanger<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }

    public static class Producer extends Thread {


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int random = new Random().nextInt();
                try {
                    exchanger.exchange(random);
                    System.out.println("Producing " + random);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            Integer random = null;

            for (int i = 0; i < 10; i++) {
                try {
                    random = exchanger.exchange(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consuming " + random);
            }

        }
    }
}
