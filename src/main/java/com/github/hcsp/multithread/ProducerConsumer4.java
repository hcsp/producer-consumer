package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
  public static void main(String[] args) throws InterruptedException {
    LinkedList<Integer> list = new LinkedList<>();
    Semaphore notFull = new Semaphore(1);
    Semaphore notEmpty = new Semaphore(0);
    Producer producer = new Producer(list, notFull, notEmpty);
    Consumer consumer = new Consumer(list, notFull, notEmpty);

    producer.start();
    consumer.start();

    producer.join();
    producer.join();
  }

  public static class Producer extends Thread {
    LinkedList<Integer> list;
    Semaphore notFull;
    Semaphore notEmpty;

    public Producer(LinkedList<Integer> list, Semaphore notFull, Semaphore notEmpty) {
      this.list = list;
      this.notFull = notFull;
      this.notEmpty = notEmpty;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        try {
          notFull.acquire();
          int r = new Random().nextInt();
          System.out.println("producing " + r);
          list.add(r);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          notEmpty.release();
        }
      }

    }
  }

  public static class Consumer extends Thread {

    LinkedList<Integer> list;
    Semaphore notFull;
    Semaphore notEmpty;

    public Consumer(LinkedList<Integer> list, Semaphore notFull, Semaphore notEmpty) {
      this.list = list;
      this.notFull = notFull;
      this.notEmpty = notEmpty;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        try {
          notEmpty.acquire();
          int value = list.get(0);
          System.out.println("Consuming " + value);
          list.remove(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          notFull.release();
        }
      }
    }
  }
}
