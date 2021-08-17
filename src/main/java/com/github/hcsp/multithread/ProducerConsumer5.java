package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) {
        Exchanger<Container> exchanger = new Exchanger<>();
        Container container1 = new Container();
        Container container2 = new Container();

        Producer producer = new Producer(exchanger, container1);
        Consumer consumer = new Consumer(exchanger, container2);

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public static class Producer implements Runnable {
        Exchanger<Container> ex;
        Container container;

        public Producer(Exchanger<Container> ex, Container container) {
            this.ex = ex;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.setValue(Optional.of(r));

                try {
                    container = ex.exchange(container);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Container> ex;
        Container container;

        public Consumer(Exchanger<Container> ex, Container container) {
            this.ex = ex;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container = ex.exchange(new Container());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consuming " + container.getValue().get());
                container.setValue(Optional.empty());
                try {
                    Thread.sleep(1);
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
