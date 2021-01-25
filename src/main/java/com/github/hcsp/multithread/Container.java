package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Optional<Integer> value = Optional.empty();

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }

    private Condition notFull;
    private Condition notEmpty;

    public Condition getNotFull() {
        return notFull;
    }

    public Condition getNotEmpty() {
        return notEmpty;
    }

    public Container(ReentrantLock lock) {
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public Container() {

    }
}
