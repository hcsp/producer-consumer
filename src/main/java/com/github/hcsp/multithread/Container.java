package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Condition NotProducedYet;//还没有被生产出来
    private Condition NotConsumedYet;//还没有被消费掉
    Optional<Integer> value = Optional.empty();

    public Container(ReentrantLock lock) {
        NotProducedYet = lock.newCondition();
        NotConsumedYet = lock.newCondition();
    }

    public Container() {
    }

    public Condition getNotProducedYet() {
        return NotProducedYet;
    }

    public Condition getNotConsumedYet() {
        return NotConsumedYet;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
