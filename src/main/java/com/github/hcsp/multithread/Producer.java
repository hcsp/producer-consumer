package com.github.hcsp.multithread;

<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> 827116c... - 修改Boss.java中的join笔误
=======
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Kelvin Chen
 */
>>>>>>> 489b8a9... 1. wait/notify实现
public class Producer extends Thread {

    private final Lock lock;
    private final Condition emptyCondition;
    private final Condition fullCondition;
    private final int SIZE = 10;
    private Stack<Integer> ret;

    public Producer(Stack<Integer> ret, Lock lock, Condition emptyCondition, Condition fullCondition) {
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
                //满位
                while (!ret.empty()) {
                    fullCondition.await();
                }

                ret.push(getRandomInt());
                emptyCondition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public int getRandomInt() {
        int randomInt = new Random().nextInt();
        System.out.println("Producing " + randomInt);
        return randomInt;
    }
}
