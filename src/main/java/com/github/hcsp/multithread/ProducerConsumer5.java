package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

// Object.wait/notify
public class ProducerConsumer5 {
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
            synchronized (container) {
                while (container.getValue().isPresent()) {
                    try {
                        container.getNotConsumer().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.setValue(Optional.of(r));
                container.notify();
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
            synchronized (container) {
                while (!container.getValue().isPresent()) {
                    try {
                        container.getNotProduce().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Integer value = container.getValue().get();
                System.out.println("Consuming " + value);
                container.setValue(Optional.empty());

                container.notify();
            }
        }
    }

}
