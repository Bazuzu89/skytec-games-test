package utils;

public class Randomizer {

    public static boolean generateBoolean() {
        return Math.random() < 0.5;
    }

    public static long generateGoldAmount() {
        return (long) (Math.random() * 100);
    }

    public static long generateClanId() {
        return (long) (Math.random() * 10 + 1);
    }
}
