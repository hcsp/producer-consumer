package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer extends Thread {
    Consumer consumer = null;
    Random random = new Random();
    Semaphore semaphore = new Semaphore(1);

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Integer num = random.nextInt();
            try {
                semaphore.acquire();
                System.out.println("Producing "+num+"");
                obverse(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void obverse(Integer num) {
        consumer.print(num, semaphore);
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
