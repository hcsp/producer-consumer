package com.github.hcsp.multithread;


import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {

        Container container = new Container();
        Exchanger<Container> exchanger = new Exchanger<>();

        Producer producer = new Producer(container, exchanger);
        Consumer consumer = new Consumer(container, exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        Exchanger<Container> exchanger;

        public Producer(Container container, Exchanger<Container> exchanger) {
            this.container = container;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.getContainer().isPresent()) {
                }
                Integer r = new Random().nextInt();
                container.setContainer(Optional.of(r));
                System.out.println("Producing " + r);
                try {
                    exchanger.exchange(container);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Exchanger<Container> exchanger;

        public Consumer(Container container, Exchanger<Container> exchanger) {
            this.container = container;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container = exchanger.exchange(container);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!container.getContainer().isPresent()) {
                }

                System.out.println("Consuming " + container.getContainer().get());
                container.setContainer(Optional.empty());
            }
        }
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();

        public Optional getContainer() {
            return container;
        }

        public void setContainer(Optional container) {
            this.container = container;
        }
    }
}
