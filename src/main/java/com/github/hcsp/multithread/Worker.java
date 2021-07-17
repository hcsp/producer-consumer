package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class Worker {
    public static int Produce(Basket basket) {
        int random = new Random().nextInt();
        basket.setValue(Optional.of(random));
        System.out.println("Producing " + random);
        return random;
    }

    public static void Consume(Basket basket) {
        System.out.println("Consuming " + basket.getValue().get());
        basket.setValue(Optional.empty());
    }
}
