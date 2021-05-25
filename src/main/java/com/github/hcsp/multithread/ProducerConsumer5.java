package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Optional<Integer>> exchanger = new Exchanger<>();
        Optional<Integer> container1 = Optional.empty();
        Optional<Integer> container2 = Optional.empty();

        Producer producer = new Producer(exchanger, container1);
        Consumer consumer = new Consumer(exchanger, container2);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Optional<Integer> container;
        Exchanger<Optional<Integer>> exchanger;

        public Producer(Exchanger<Optional<Integer>> exchanger, Optional<Integer> container) {
            this.exchanger = exchanger;
            this.container = container;

        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (!container.isPresent()) {
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container = Optional.of(r);
                    try {
                        container = exchanger.exchange(container);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Optional<Integer> container;
        Exchanger<Optional<Integer>> exchanger;

        public Consumer(Exchanger<Optional<Integer>> exchanger, Optional<Integer> container) {
            this.exchanger = exchanger;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                try {
                    container = exchanger.exchange(container);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int r = container.get();
                System.out.println("Consuming " + r);
                container = Optional.empty();

            }
        }
    }


}
