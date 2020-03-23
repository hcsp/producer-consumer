package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

//感觉场景不是太合适,感觉跟第一种方法一样
public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        Container container = new Container();
        Producer producer = new Producer(semaphore, container);
        Consumer consumer = new Consumer(semaphore, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();

        public Container() {
        }

        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional<Integer> container) {
            this.container = container;
        }
    }


    public static class Producer extends Thread {
        Semaphore semaphore;
        Container container;

        public Producer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.getContainer().isPresent()) {
                    semaphore.release();
                }

                try {
                    semaphore.acquire();
                    int r = new Random().nextInt();
                    container.setContainer(Optional.of(r));
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Semaphore semaphore;
        Container container;

        public Consumer(Semaphore semaphore, Container container) {
            this.semaphore = semaphore;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (!container.getContainer().isPresent()) {
                    semaphore.release();
                }

                try {
                    semaphore.acquire();
                    System.out.println("Consuming " + container.getContainer().get());
                    container.setContainer(Optional.empty());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

            }
        }
    }
}
