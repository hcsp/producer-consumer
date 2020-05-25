package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Exchanger<Container> exchange = new Exchanger<>();


        Producer producer = new Producer(container, exchange);
        Consumer consumer = new Consumer(container, exchange);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
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

    public static class Producer extends Thread {
        private Container container;
        private Exchanger<Container> exchanger;

        public Producer(Container container, Exchanger<Container> exchanger) {
            this.container = container;
            this.exchanger = exchanger;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    while (!exchanger.exchange(container).getValue().isPresent()) {
                        int r = new Random().nextInt();
                        System.out.println("Producer " + r);
                        exchanger.exchange(container).setValue(Optional.of(r));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Container container;
        private Exchanger<Container> exchanger;

        public Consumer(Container container, Exchanger<Container> exchanger) {
            this.container = container;
            this.exchanger = exchanger;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    while (exchanger.exchange(container).getValue().isPresent()) {
                        System.out.println("Consumer " + exchanger.exchange(container).getValue().get());
                        exchanger.exchange(container).setValue(Optional.empty());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
