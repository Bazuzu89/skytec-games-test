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

    public static long generateUserId() {
        return (long) (Math.random() * 11 + 1);
    }

    public static long generateTaskId() {
        return (long) (Math.random() * 100 + 1);
    }
}
