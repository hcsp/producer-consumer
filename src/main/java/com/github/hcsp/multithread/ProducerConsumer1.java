package com.github.hcsp.multithread;

public class ProducerConsumer1 {
    public static int condition = 0; //判断盘子是否为空，1为不空，0为空
    public static Object lock = new Object();
    public static int apple = 0;  //盘子中存的数

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
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (condition == 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    apple = (int) (1 + Math.random() * 10);
                    System.out.println("Producing " + apple);
                    condition = 1;
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (condition == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + apple);
                    condition = 0;
                    lock.notify();
                }
            }
        }
    }
}
