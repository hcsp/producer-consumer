package com.github.hcsp.multithread;

import java.util.Random;

public class Producer extends Thread {
    private final CubbyHole hole;

    public Producer(CubbyHole hole) {
        this.hole = hole;
    }

    @Override
    public void run() {
        int forCount = hole.getForCount();
        int i = 0;
        synchronized (hole) {
            do {
                hole.setContents(new Random().nextInt());
                System.out.println("Producing " + hole.getContents());

                hole.notifyAll();
                try {
                    hole.wait();
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (hole.getContents() == 0 && i < forCount);


        }

    }
}
