package com.github.hcsp.multithread;

public class Box<T> {
    private T value;

    public T take() {
        T copiedValue = value;
        this.value = null;
        return copiedValue;
    }

    public void put(T value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null;
    }
}
