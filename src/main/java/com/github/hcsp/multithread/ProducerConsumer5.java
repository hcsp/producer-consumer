package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Container container = new Container();
        Producer producer = new Producer(exchanger, container);
        Consumer consumer = new Consumer(exchanger, container);

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
                int r = new Random().nextInt();
                container.setValue(Optional.of(r));
                try {
                    Thread.sleep(1000);
                    exchanger.exchange(r);
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Integer> exchanger;
        Container container;

        public Consumer(Exchanger<Integer> exchanger, Container container) {
            this.exchanger = exchanger;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    Integer value = exchanger.exchange(null);
                    System.out.println("Consuming " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class Container {
        Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }


}
