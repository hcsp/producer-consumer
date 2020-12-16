package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Container> integerExchanger = new Exchanger<>();
        Producer producer = new Producer(integerExchanger);
        Consumer consumer = new Consumer(integerExchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Exchanger<Container> integerExchanger;
        Container container;

        public Producer(Exchanger<Container> integerExchanger) {
            this.integerExchanger = integerExchanger;
        }

        @Override
        public void run() {
            int data;
            for (int i = 0; i < 10; i++) {
                try {
                    data = new Random().nextInt();
                    container.setValue(Optional.of(data));
                    integerExchanger.exchange(container);
                    System.out.println("Producing " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Container> integerExchanger;
        Container container;
        int data;

        public Consumer(Exchanger<Container> integerExchanger) {
            this.integerExchanger = integerExchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container = new Container();
                    container.setValue(Optional.empty());
                    Container answer = integerExchanger.exchange(container);
                    data = answer.getValue().get();
                    System.out.println("Consuming " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
