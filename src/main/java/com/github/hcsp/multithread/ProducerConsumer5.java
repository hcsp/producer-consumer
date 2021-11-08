package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Optional<Integer>> exchanger = new Exchanger<>();
        Exchanger<Boolean> single = new Exchanger<>();
        Producer producer = new Producer(exchanger, single);
        Consumer consumer = new Consumer(exchanger, single);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Optional<Integer> container = Optional.empty();
        Exchanger<Optional<Integer>> exchanger;
        Exchanger<Boolean> single;

        public Producer(Exchanger<Optional<Integer>> exchanger, Exchanger<Boolean> single) {
            this.exchanger = exchanger;
            this.single = single;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (!container.isPresent()) {
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container = Optional.of(r);
                }
                try {
                    container = exchanger.exchange(container); // 交换容器，如果对方没有到达交换点则阻塞
                    single.exchange(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Optional<Integer> container = Optional.empty();
        Exchanger<Optional<Integer>> exchanger;
        Exchanger<Boolean> single;

        public Consumer(Exchanger<Optional<Integer>> exchanger, Exchanger<Boolean> single) {
            this.exchanger = exchanger;
            this.single = single;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container = exchanger.exchange(container); // 交换容器，如果对方没有到达交换点则阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (container.isPresent()) {
                    System.out.println("Consuming " + container.get());
                    container = Optional.empty();
                    try {
                        single.exchange(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
