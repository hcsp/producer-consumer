package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
  public static void main(String[] args) throws InterruptedException {
    LinkedBlockingQueue<Integer> list = new LinkedBlockingQueue<>(1);
    Producer producer = new Producer(list);
    Consumer consumer = new Consumer(list);

    producer.start();
    consumer.start();

    producer.join();
    producer.join();
  }


  public static class Producer extends Thread {
    LinkedBlockingQueue<Integer> list;

    public Producer(LinkedBlockingQueue<Integer> list) {
      this.list = list;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        int r = new Random().nextInt();
        try {
          list.put(r);
          System.out.println("Producing " + r);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static class Consumer extends Thread {
    LinkedBlockingQueue<Integer> list;

    public Consumer(LinkedBlockingQueue<Integer> list) {
      this.list = list;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10; i++) {
        try {
          int value = list.take();
          System.out.println("Consuming " + value);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
