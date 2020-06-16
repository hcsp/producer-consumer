package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    private static Exchanger<Container> exchanger = new Exchanger();

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int nextInt = new Random().nextInt();
                System.out.println("Producing " + nextInt);
                container.setValue(Optional.of(nextInt));
                try {
                    while (container.getValue().isPresent()) {
                        container = exchanger.exchange(container);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container = exchanger.exchange(container);
                    if (container.getValue().isPresent()) {
                       System.out.println("Consuming " + container.getValue().get());
                       container.setValue(Optional.empty());
                       exchanger.exchange(container);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
