package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Condition notConsumedYet;
    private Condition notProducedYet;


    public Container(ReentrantLock lock) {
        this.notConsumedYet = lock.newCondition();
        this.notProducedYet = lock.newCondition();
    }

    public Container() {
    }

    public Condition getNotProducedYet() {
        return notProducedYet;
    }

    public Condition getNotConsumedYet() {
        return notConsumedYet;
    }

    private Optional<Integer> container = Optional.empty();

    public Optional<Integer> getContainer() {
        return container;
    }

    public void setContainer(Optional<Integer> container) {
        this.container = container;
    }
}
