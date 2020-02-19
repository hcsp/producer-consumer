package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yaohengfeng
 * @version 1.0
 * @date 2020/2/19 12:07
 */
public class Container {
    private Optional<Integer> vaule = Optional.empty();
    private Condition notConsumerCondition;
    private Condition notProducerCondition;

    Container() {
    }

    Container(ReentrantLock lock) {
        this.notConsumerCondition = lock.newCondition();
        this.notProducerCondition = lock.newCondition();
    }

    public Condition getNotConsumerCondition() {
        return notConsumerCondition;
    }

    public void setNotConsumerCondition(Condition notConsumerCondition) {
        this.notConsumerCondition = notConsumerCondition;
    }

    public Condition getNotProducerCondition() {
        return notProducerCondition;
    }

    public void setNotProducerCondition(Condition notProducerCondition) {
        this.notProducerCondition = notProducerCondition;
    }

    public Optional<Integer> getVaule() {
        return vaule;
    }

    public void setVaule(Optional<Integer> vaule) {
        this.vaule = vaule;
    }
}
