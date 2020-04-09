package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Optional<Integer> value = Optional.empty();
    private Condition notProducedYet;
    private Condition notConsumedYet;

    public Container() {
    }

    public Container(ReentrantLock lock) {
        this.notProducedYet = lock.newCondition();
        this.notConsumedYet = lock.newCondition();
    }

    public Condition getNotProducedYet() {
        return notProducedYet;
    }

    public Condition getNotConsumedYet() {
        return notConsumedYet;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
