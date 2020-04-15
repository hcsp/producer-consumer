package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Condition notConsumer;
    private Condition notProduce;

    public Container() {
    }

    Container(ReentrantLock lock) {
        this.notConsumer = lock.newCondition();
        this.notProduce = lock.newCondition();
    }

    public Condition getNotConsumer() {
        return notConsumer;
    }

    public void setNotConsumer(Condition notConsumer) {
        this.notConsumer = notConsumer;
    }

    public Condition getNotProduce() {
        return notProduce;
    }

    public void setNotProduce(Condition notProduce) {
        this.notProduce = notProduce;
    }


    Optional<Integer> value = Optional.empty();

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
