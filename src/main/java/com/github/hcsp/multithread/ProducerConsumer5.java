package com.github.hcsp.multithread;

import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {
    private static final Exchanger<Integer> exchanger = new Exchanger<Integer>();
    private static final Basket basket = new Basket();
    private static int index = 0;

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            new CreateRunInstance().run(Type.PRODUCE);
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            new CreateRunInstance().run(Type.CONSUME);
        }
    }


    private static class CreateRunInstance implements CreateRun {
        @Override
        public void run(Enum<Type> type) {
            while (index < 10) {
                try {
                    if (type == Type.CONSUME) {
                        consume();
                    } else {
                        produce();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void produce() throws InterruptedException {
            if (!basket.getValue().isPresent()) {
                int random = Worker.Produce(basket);
                exchanger.exchange(random);
            }
        }

        @Override
        public void consume() throws InterruptedException {
            if (basket.getValue().isPresent()) {
                Worker.Consume(basket);
                index++;
                exchanger.exchange(1);
            }
        }
    }
}
