package com.github.hcsp.multithread;

public interface CreateRun {
    void run(Enum<Type> type);

    void produce() throws InterruptedException;

    void consume() throws InterruptedException;
}
