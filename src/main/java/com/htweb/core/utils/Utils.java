package com.htweb.core.utils;

public class Utils {

    public static int parseInt(String input, int defaultVal) {
        if (input == null || input.isEmpty()) {
            return defaultVal;
        }

        return Integer.parseInt(input);
    }

    public static Integer parseInt(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Integer.valueOf(input);
    }

    public static long parseLong(String input, long defaultVal) {
        if (input == null || input.isEmpty()) {
            return defaultVal;
        }

        return Long.parseLong(input);
    }

    public static Long parseLong(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Long.valueOf(input);
    }

    public static boolean parseBool(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        return Boolean.parseBoolean(input);
    }
}
