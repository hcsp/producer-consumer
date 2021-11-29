package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {

        Exchanger<Container> exchanger = new Exchanger<Container>();
        Container container0 = new Container();
        Container container1 = new Container();

        Producer producer = new Producer(exchanger, container0);
        Consumer consumer = new Consumer(exchanger, container1);


        producer.start();
        consumer.start();


        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container0;
        Exchanger<Container> exchanger;

        public Producer(Exchanger exchanger, Container container) {
            this.exchanger = exchanger;
            this.container0 = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {


                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container0.setOptional(Optional.of(r));
                try {
                    container0 = exchanger.exchange(container0);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        Exchanger<Container> exchanger;
        Container container1;

        public Consumer(Exchanger exchanger, Container container) {
            this.exchanger = exchanger;
            this.container1 = container;
        }

        @Override
        public void run() {
            for (int j = 0; j < 10; j++) {

                try {
                    container1 = exchanger.exchange(container1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Integer a = container1.optional.get();
                System.out.println("Consuming " + a);
                container1.setOptional(Optional.empty());

            }
        }
    }

    static class Container {
        private Optional<Integer> optional = Optional.empty();

        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
    }
}
