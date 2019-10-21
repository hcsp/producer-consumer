package com.github.hcsp.multithread;

import java.util.concurrent.Semaphore;

public class Consumer {
    public void print(Integer num, Semaphore semaphore) {
        System.out.println("Consuming "+num+"");
        semaphore.release(1);
    }
}
