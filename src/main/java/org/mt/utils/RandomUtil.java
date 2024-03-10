package org.mt.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {

    static String CRUD_PREFIX = "crud-";

    public static <T> T getRandomFrom(List<T> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static <T> List<T> getRandomListFrom(List<T> list, int size) {
        Random random = new Random();
        List<T> randomList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(list.size());
            while(randomList.contains(list.get(randomIndex)))
                randomIndex = random.nextInt(list.size());
            randomList.add(list.get(randomIndex));
        }
        return randomList;
    }

    public static <T> T getRandomFrom(T[] list) {
        Random random = new Random();
        return list[random.nextInt(list.length)];
    }

    public static <T> T getRandomFromExcludeFirst(List<T> list, int first) {
        int maximum = list.size() - 1;
        int random = getRandomIntBetween(first, maximum);
        return list.get(random);
    }

    public static <T> T getRandomFromExcludeFirst(T[] list, int first) {
        int maximum = list.length - 1;
        int random = getRandomIntBetween(first, maximum);
        return list[random];
    }

    public static int getRandomIntBetween(int minimum, int maximum) {
        Random random = new Random();
        return minimum + random.nextInt((maximum - minimum) + 1);
    }

    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static String getRandomString(int length) {
        var count = length - CRUD_PREFIX.length();
        return CRUD_PREFIX + RandomStringUtils.randomAlphabetic(count).toLowerCase();
    }

    public static String getRandomStringStartsWith(String startsWith, int length) {
        var count = length - startsWith.length();
        return startsWith + RandomStringUtils.randomAlphabetic(count).toLowerCase();
    }
}
