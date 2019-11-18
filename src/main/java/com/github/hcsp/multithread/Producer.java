package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
    private BlockingQueue<Integer> que;

    public Producer(BlockingQueue<Integer> que) {
        this.que = que;
    }

    @Override
    public void run() {
        for (int i=0;i<10;i++){
            int r = new Random().nextInt();
            try {
                que.put(r);
                System.out.println("Producing " + r);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
