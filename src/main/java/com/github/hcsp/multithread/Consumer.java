package com.github.hcsp.multithread;

public class Consumer extends Thread {

    private Container container;

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
            System.out.println("Consuming " + container.value);
            container.value = null;
            container.notifyAll();
        }
    }
}
