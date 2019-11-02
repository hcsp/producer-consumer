package com.github.hcsp.multithread;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author wheelchen
 */
public class Consumer extends Thread {

    private final Lock lock;
    private final Condition emptyCondition;
    private final Condition fullCondition;
    private Stack<Integer> ret;
    private final int SIZE = 10;

    public Consumer(Stack<Integer> ret, Lock lock, Condition emptyCondition, Condition fullCondition) {
        this.ret = ret;
        this.lock = lock;
        this.emptyCondition = emptyCondition;
        this.fullCondition = fullCondition;
    }

    @Override
    public void run() {
        for (int i = 0; i < SIZE; i++) {
            try {
                lock.lock();
                //位空
                while (ret.empty()) {
                    emptyCondition.await();
                }
                int random = ret.pop();
                System.out.println("Consuming " + random);

                fullCondition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
