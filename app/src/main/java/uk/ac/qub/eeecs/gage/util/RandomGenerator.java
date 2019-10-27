package uk.ac.qub.eeecs.gage.util;

import java.util.Random;

public class RandomGenerator {
    public static float getRandomNum() {
        Random rand = new Random();
        return (rand.nextInt(20) + 10);
    }
}
