package com.github.hcsp.multithread;

import java.util.Random;


public class Producer extends Thread {
    private int size=10;
    @Override
    public void run() {
        while (size>0) {
            synchronized (Boss.class) {
                if (!Boss.flag) {
                    System.out.println("Producing " + new Random().nextInt());
                    size--;
                    Boss.flag = true;
                    Boss.class.notify();
                } else {
                    try {
                        Boss.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
