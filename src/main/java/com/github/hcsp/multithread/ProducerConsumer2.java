package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
  public static void main(String[] args) throws InterruptedException {
    ReentrantLock lock = new ReentrantLock();
    Store store = new Store();

    Condition full = lock.newCondition();
    Condition empty = lock.newCondition();
    Producer producer = new Producer(lock, store, full, empty);
    Consumer consumer = new Consumer(lock, store, full, empty);

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
    ReentrantLock lock;
    Store store;
    Condition full;
    Condition empty;

    public Producer(ReentrantLock lock, Store store, Condition full, Condition empty) {
      this.lock = lock;
      this.store = store;
      this.full = full;
      this.empty = empty;
    }

    @Override
    public void run() {

      for (int i = 0; i < 10; i++) {
        lock.lock();
        while (store.getValue().isPresent()) {
          try {
            full.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        int r = new Random().nextInt();
        System.out.println("Producing " + r);
        store.setValue(Optional.of(r));
        empty.signalAll();
        lock.unlock();
      }
    }
  }

  public static class Consumer extends Thread {
    ReentrantLock lock;
    Store store;
    Condition full;
    Condition empty;

    public Consumer(ReentrantLock lock, Store store, Condition full, Condition empty) {
      this.lock = lock;
      this.store = store;
      this.full = full;
      this.empty = empty;
    }

    @Override
    public void run() {

      for (int i = 0; i < 10; i++) {
        lock.lock();
        while (!store.getValue().isPresent()) {
          try {
            empty.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        Integer value = store.getValue().get();
        System.out.println("Consuming " + value);
        store.setValue(Optional.empty());
        full.signalAll();
        lock.unlock();
      }
    }
  }
}



