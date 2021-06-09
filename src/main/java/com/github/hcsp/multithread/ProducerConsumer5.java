package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Container container = new Container();
        Producer producer = new Producer(exchanger, container);
        Consumer consumer = new Consumer(exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Exchanger<Integer> exchanger;
        Container container;

        public Producer(Exchanger<Integer> exchanger, Container container) {
            this.exchanger = exchanger;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int random = new Random().nextInt();
                try {
                    int exchange = exchanger.exchange(random);
                    System.out.println("Producing " + random);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Integer> exchanger;

        public Consumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            int a = -1;
            for (int i = 0; i < 10; i++) {
                try {
                    int exchange = exchanger.exchange(a);
                    System.out.println("Consuming " + exchange);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Container {

        private Optional<Integer> sharedData = Optional.empty();

        public Optional<Integer> getSharedData() {
            return sharedData;
        }

        public void setSharedData(Optional<Integer> sharedData) {
            this.sharedData = sharedData;
        }
    }
}
