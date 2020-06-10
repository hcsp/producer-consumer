package com.github.hcsp.multithread;

import com.github.hcsp.multithread.ProducerConsumer2.Producer.Consumer;
import com.github.hcsp.multithread.ProducerConsumer2.Producer.Container;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

  public static void main(String[] args) throws InterruptedException {
    Container container = new Container();
    Producer producer = new Producer(container);
    Consumer consumer = new Consumer(container);

    producer.start();
    consumer.start();

    producer.join();
    producer.join();
  }

  public static class Producer extends Thread {

    private final Container container;

    public Producer(Container container) {
      this.container = container;
    }

    @Override
    public void run() {
      ReentrantLock lock = container.getLOCK();
      for (int i = 0; i < 10; i++) {
        lock.lock();
        try {
          if (container.getItem() != null) {
            container.get容器内容为空().await();
          }
          // 生产
          Integer r = new Random().nextInt();
          container.setItem(r);
          System.out.println("Producing " + r);
          container.get容器内容已满().signal();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
        }
      }
    }

    public static class Consumer extends Thread {

      private final Container container;

      public Consumer(Container container) {
        this.container = container;
      }

      @Override
      public void run() {
        ReentrantLock lock = container.getLOCK();
        for (int i = 0; i < 10; i++) {
          lock.lock();
          try {
            if (container.getItem() == null) {
              container.get容器内容已满().await();
            }
            // 消费
            System.out.println("Consuming " + container.getItem());
            container.setItem(null);
            container.get容器内容为空().signal();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            lock.unlock();
          }
        }
      }
    }

    static class Container {

      private final ReentrantLock LOCK = new ReentrantLock();
      private final Condition 容器内容为空 = LOCK.newCondition();
      private final Condition 容器内容已满 = LOCK.newCondition();

      private Integer item;

      public Integer getItem() {
        return item;
      }

      public void setItem(Integer item) {
        this.item = item;
      }

      public ReentrantLock getLOCK() {
        return LOCK;
      }

      public Condition get容器内容为空() {
        return 容器内容为空;
      }

      public Condition get容器内容已满() {
        return 容器内容已满;
      }

    }
  }
}

