package com.github.hcsp.multithread;

public class Consumer extends Thread {
    private final CubbyHole hole;

    public Consumer(CubbyHole hole) {
        this.hole = hole;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (hole) {
                System.out.println("Consuming " + hole.getContents());
                hole.setContents(0);
                hole.notifyAll();

                try {
                    hole.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
