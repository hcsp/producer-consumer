package com.github.hcsp.multithread;

import java.util.List;
import java.util.Random;

public class Worker {
    public static int Produce(List<Integer> list) {
        int random = new Random().nextInt();
        list.add(random);
        System.out.println("Producing " + list.get(0));
        return random;
    }

    public static void Consume(List<Integer> list) {
        System.out.println("Consuming " + list.get(0));
        list.remove(0);
    }
}
