package com.github.hcsp.multithread;


import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container2 container = new Container2(lock);
        Producer2 producer = new Producer2(container, lock);
        Consumer2 consumer = new Consumer2(container, lock);

        producer.start();
        consumer.start();


        producer.join();
        producer.join();
    }
}

class Producer2 extends Thread {
    Container2 container;
    ReentrantLock lock;

    Producer2(Container2 container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                while ((container.getValue().isPresent())) {
                    try {
                        container.getNotProducedYet().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing" + r);
                container.setValue(Optional.of(r));
                container.getNotConsummedYet().signal();
            } finally {
                lock.unlock();
            }
        }
    }
}

class Consumer2 extends Thread {
    Container2 container;
    ReentrantLock lock;

    Consumer2(Container2 container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                while (!container.getValue().isPresent()) {
                    try {
                        container.getNotConsummedYet().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer value = container.getValue().get();
                container.setValue(Optional.empty());
                System.out.println("Consuming" + value);
                container.getNotProducedYet().signal();
            } finally {
                lock.unlock();
            }
        }
    }
}

class Container2 {
    private Condition notConsummedYet;
    private Condition notProducedYet;

    Container2(ReentrantLock lock) {
        this.notConsummedYet = lock.newCondition();
        this.notProducedYet = lock.newCondition();
    }

    public Condition getNotConsummedYet() {
        return notConsummedYet;
    }

    public void setNotConsummedYet(Condition notConsummedYet) {
        this.notConsummedYet = notConsummedYet;
    }

    public Condition getNotProducedYet() {
        return notProducedYet;
    }

    public void setNotProducedYet(Condition notProducedYet) {
        this.notProducedYet = notProducedYet;
    }

    private Optional<Integer> value = Optional.empty();

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}

