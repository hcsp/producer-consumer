package com.github.hcsp.multithread;

<<<<<<< HEAD
=======
<<<<<<< HEAD
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

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
public class Consumer extends Thread {
    @Override
    public void run() {}
>>>>>>> fc56ff0... - 修改Boss.java中的join笔误
>>>>>>> 827116c... - 修改Boss.java中的join笔误
}
