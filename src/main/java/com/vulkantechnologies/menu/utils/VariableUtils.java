package com.vulkantechnologies.menu.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VariableUtils {

    public static Object parseValue(String str, Class<?> type) {
        if (type == Integer.class)
            return Integer.parseInt(str);
        else if (type == Double.class)
            return Double.parseDouble(str);
        else if (type == Long.class)
            return Long.parseLong(str);
        else if (type == Float.class)
            return Float.parseFloat(str);
        else if (type == Boolean.class)
            return Boolean.parseBoolean(str);
        return str;
    }

    public static Class<?> getType(String str) {
        if (isInteger(str))
            return Integer.class;
        else if (isDouble(str))
            return Double.class;
        else if (isLong(str))
            return Long.class;
        else if (isFloat(str))
            return Float.class;
        else if (isBoolean(str))
            return Boolean.class;
        return String.class;
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }
}
