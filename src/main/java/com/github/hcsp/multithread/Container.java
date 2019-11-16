package com.github.hcsp.multithread;

import java.util.Optional;

public class Container {
    Optional<Integer> value = Optional.empty();


    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
