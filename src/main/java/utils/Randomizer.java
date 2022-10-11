package utils;

public class Randomizer {

    public static boolean generateBoolean() {
        return Math.random() < 0.5;
    }

    public static long generateGoldAmount() {
        return (long) (Math.random() * 100);
    }
}
