package com.github.hcsp.multithread;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> 9389a11... 1. lock/condition 实现
=======
>>>>>>> 827116c... - 修改Boss.java中的join笔误
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

<<<<<<< HEAD
/**
 * @author wheelchen
 */
>>>>>>> 827116c... - 修改Boss.java中的join笔误
public class Consumer extends Thread {
    @Override
<<<<<<< HEAD
    public void run() {}
=======
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
=======
=======
=======
>>>>>>> 9389a11... 1. lock/condition 实现
/**
 * @author wheelchen
 */
>>>>>>> 489b8a9... 1. wait/notify实现
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
<<<<<<< HEAD
    public void run() {}
>>>>>>> fc56ff0... - 修改Boss.java中的join笔误
>>>>>>> 827116c... - 修改Boss.java中的join笔误
=======
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
<<<<<<< HEAD
<<<<<<< HEAD

    private void safeWait() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
>>>>>>> 489b8a9... 1. wait/notify实现
=======
>>>>>>> 9389a11... 1. lock/condition 实现
=======
=======
public class Consumer extends Thread {
    @Override
    public void run() {}
>>>>>>> fc56ff0... - 修改Boss.java中的join笔误
>>>>>>> 827116c... - 修改Boss.java中的join笔误
}
