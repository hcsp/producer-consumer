package com.github.hcsp.multithread;

import java.util.Queue;
import java.util.Random;

public class Producer extends Thread {

    private final Queue<Integer> queue;

    public Producer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1; i++) {
            synchronized (queue) {
                while (!queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int random = new Random().nextInt();
                queue.offer(random);
                System.out.println(random);
                queue.notify();
            }
        }
    }
}
