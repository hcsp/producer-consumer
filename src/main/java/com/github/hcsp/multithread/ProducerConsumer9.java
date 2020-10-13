package com.github.hcsp.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Phaser;

/***
 *  使用Phaser
 *  需要自旋
 *  @author gongxuanzhang
 **/
public class ProducerConsumer9 {

    static Phaser phaser = new ProduceAndConsume(10);

    static Queue<Integer> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }


    public static class ProduceAndConsume extends Phaser {
        // 表示生产者消费者需要创建多少次
        final int num;

        public ProduceAndConsume(int num) {
            this.num = num;
        }

        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            return phase == this.num;
        }
    }

    public static class Producer extends Thread {


        @Override
        public void run() {
            phaser.register();
            int random = new Random().nextInt();
            queue.offer(random);
            System.out.println("Producing " + random);
            phaser.arriveAndAwaitAdvance();
            for (int i = 0; i < 9; i++) {
                random = new Random().nextInt();
                queue.offer(random);
                System.out.println("Producing " + random);
                phaser.arriveAndAwaitAdvance();
            }
        }
    }

    public static class Consumer extends Thread {

        @Override
        public void run() {
            phaser.register();
            for (int i = 0; i < 10; i++) {
                phaser.arriveAndAwaitAdvance();
                System.out.println("Consuming " + queue.poll());
            }

        }
    }
}
