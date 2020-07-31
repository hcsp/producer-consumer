package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerConsumer5 {

    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Exchanger<String> signalExchanger = new Exchanger<>();
        Producer producer = new Producer(exchanger, signalExchanger);
        Consumer consumer = new Consumer(exchanger, signalExchanger);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private Exchanger<Integer> exchanger;
        private Exchanger<String> signalExchanger;

        public Producer(Exchanger<Integer> exchanger, Exchanger<String> signalExchanger) {
            this.exchanger = exchanger;
            this.signalExchanger = signalExchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    exchanger.exchange(r);
                    signalExchanger.exchange("wait for consumer consuming!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Exchanger<Integer> exchanger;
        private Exchanger<String> signalExchanger;

        public Consumer(Exchanger<Integer> exchanger, Exchanger<String> signalExchanger) {
            this.exchanger = exchanger;
            this.signalExchanger = signalExchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer exchangeVal = exchanger.exchange(null);
                    System.out.println("Consuming " + exchangeVal);
                    signalExchanger.exchange("I‘m consumer. I have consumed!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
