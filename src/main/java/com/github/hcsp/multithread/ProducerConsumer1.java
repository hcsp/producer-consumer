package com.github.hcsp.multithread;

public class ProducerConsumer1 {
    private static final Object lock = new Object();
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
                synchronized (lock) {
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
        }

        @Override
        public void produce() throws InterruptedException {
            while (basket.getValue().isPresent()) {
                lock.wait();
            }
            Worker.Produce(basket);
            lock.notify();
        }

        @Override
        public void consume() throws InterruptedException {
            while (!basket.getValue().isPresent()) {
                lock.wait();
            }
            Worker.Consume(basket);
            index++;
            lock.notify();
        }
    }
}
