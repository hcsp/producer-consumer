package com.github.hcsp.multithread;

import java.util.Random;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();

        Thread producer1 = new Thread(new Producer(container));
        Thread producer2 = new Thread(new Producer(container));
        Thread producer3 = new Thread(new Producer(container));

        producer1.start();
        Thread.sleep(100);

        for (int i = 0; i < 100; i++) {
            new Thread(new Consumer(container)).start();
        }

        producer2.start();
        Thread.sleep(100);
        producer3.start();
        Thread.sleep(100);
    }

    static class Container {
        Object value;
    }

    static class Producer implements Runnable {
        Container container;


        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                while (container.value != null) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int random = new Random().nextInt();
                container.value = random;
                System.out.println(random);
                container.notify();
            }
        }
    }

    static class Consumer implements Runnable {
        Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                while (container.value == null) {
                    try {
                        container.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(container.value);
                container.value = null;
                container.notify();
            }
        }
    }
}