package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * 使用Exchanger
 * 这个题的原理很简单，exchanger只存一个数便会阻塞，等另一个线程再存一个数，达到2个数，便不会阻塞，交换这两个数并继续往下执行
 * 原理很简单，但是大多数同学的输出其实是不对的，表面上通过了测试，实际上存在问题，如果给每次输出时打上标签，可以清楚的看到，在第一轮交换过后
 * 打印出的语句顺序发生了改变，如下图，生产者与消费者的的数不是按给定顺序一一对应的，生产者连续生产了2次，消费者又连续消费了2次
 * <p>
 * 虚假的AC格式:
 * 生产交换前  Producing 1238676230
 * 消费交换后  Consuming 1238676230
 * <p>
 * 生产交换前  Producing 469136470
 * 生产交换前  Producing 387274866
 * 消费交换后  Consuming 469136470
 * <p>
 * 消费交换后  Consuming 387274866
 * <p>
 * 而产生这种情况的原因很简单：
 * 由于存入2个数才更新，在开始时生产者先存数，消费者后存数，一轮交换后消费者拿到数，此时消费者继续进行下一次循环，消费者此时变成了先存数的人，消费者存入数后阻塞，
 * 生产者变成了后存数的人，生产者在存数之前先打印一下得到的随机数，在存完数后，凑够2个数进行交换，生产者继续进入下一次循环，生产者此时拿到新的随机数，便再打印一次
 * 于是就出现了第一轮过后，生产者连生2次，消费者又连续消费2次这种错位的顺序
 * 解决这种问题的思路就是让生产者每次都是先存数的人，所以可以在每次交换后在末尾再加一次多余的交换进行修正
 * 如下图，便可以得到正确的输出顺序
 * 打印出的正确的格式：
 * 生产交换前  Producing 32719045
 * 消费交换后  Consuming 32719045
 * <p>
 * 生产交换前  Producing -1733038944
 * 消费交换后  Consuming -1733038944
 * <p>
 * 生产交换前  Producing 519625313
 * 消费交换后  Consuming 519625313
 */
public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<Integer>();
        Producer producer = new Producer("", exchanger);
        Consumer consumer = new Consumer("", exchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static final  LinkedList<Integer> lists = new LinkedList<>();

    public static class Producer extends Thread {
        private Exchanger<Integer> exchanger;

        public Producer(String name, Exchanger<Integer> exchanger) {
            super("Producing " + name);
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int message = new Random().nextInt();
                    System.out.println("生产交换前  " + getName() + message);
                    exchanger.exchange(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    exchanger.exchange(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        private Exchanger<Integer> exchanger;
        int data;

        public Consumer(String name, Exchanger<Integer> exchanger) {
            super("Consuming " + name);
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    data = exchanger.exchange(null);
                    System.out.println("消费交换后  " + getName() + data);
                    System.out.println("   ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    exchanger.exchange(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
