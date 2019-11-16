package com.github.hcsp.multithread;

import java.util.Queue;

public class Consumer extends Thread {

    private final Queue<Integer> queue;

    public Consumer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1; i++) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer value = queue.poll();
                System.out.println(value);
                queue.notify();
            }
        }
    }
}
