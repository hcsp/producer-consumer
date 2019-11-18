package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    Optional<Integer> container = Optional.empty();
    private Condition notConsumedYet;
    private Condition notProducedYet;

    public Container(ReentrantLock lock) {
        notConsumedYet = lock.newCondition();
        notProducedYet = lock.newCondition();
    }

    public void put(Integer value) {
        container = Optional.of(value);
    }

    public int take() {
        int value = container.get();
        container = Optional.empty();
        return value;
    }

    public boolean isPresent() {
        return container.isPresent();
    }

    public Condition getNotConsumedYet() {
        return notConsumedYet;
    }

    public void setNotConsumedYet(Condition notConsumedYet) {
        this.notConsumedYet = notConsumedYet;
    }

    public Condition getNotProducedYet() {
        return notProducedYet;
    }

    public void setNotProducedYet(Condition notProducedYet) {
        this.notProducedYet = notProducedYet;
    }
}
