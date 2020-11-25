package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {

  public static void main(String[] args) throws InterruptedException {


    Store store = new Store();
    Object lock = new Object();
    Producer producer = new Producer(store, lock);
    Consumer consumer = new Consumer(store, lock);

    producer.start();
    consumer.start();

    producer.join();
    producer.join();
  }

  public static class Store {
    Optional<Integer> value = Optional.empty();

    public void setValue(Optional<Integer> value) {
      this.value = value;
    }

    public Optional<Integer> getValue() {
      return value;
    }
  }

  public static class Producer extends Thread {
    Store store;
    Object lock;

    public Producer(Store store, Object lock) {
      this.store = store;
      this.lock = lock;
    }


    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        synchronized (lock) {
          while (store.getValue().isPresent()) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          int r = new Random().nextInt();
          System.out.println("Producing " + r);
          store.setValue(Optional.of(r));
          lock.notifyAll();
        }
      }
    }
  }

  public static class Consumer extends Thread {
    Store store;
    Object lock;

    public Consumer(Store store, Object lock) {
      this.store = store;
      this.lock = lock;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        synchronized (lock) {
          while (!store.getValue().isPresent()) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          Integer value = store.getValue().get();
          System.out.println("Consuming " + value);
          store.setValue(Optional.empty());
          lock.notifyAll();
        }
      }
    }
  }
}
