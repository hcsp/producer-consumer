package com.github.hcsp.multithread;

import java.util.List;
import java.util.Random;

public class Worker {
    public static void Produce(List<Integer> list) {
        list.add(new Random().nextInt());
        System.out.println("Producing " + list.get(0));
    }

    public static void Consume(List<Integer> list) {
        System.out.println("Consuming " + list.get(0));
        list.remove(0);
    }
}
