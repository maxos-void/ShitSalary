package com.github.maxos.shitSalary.random;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPay {

    private final ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public double getRandomPay(double min, double max) {
        return rnd.nextDouble(min, max + 1);
    }

}
