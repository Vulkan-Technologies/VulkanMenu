package com.vulkantechnologies.menu.utils;

import com.vulkantechnologies.menu.configuration.adapter.compact.CompactBooleanAdapter;
import com.vulkantechnologies.menu.configuration.adapter.compact.CompactDoubleAdapter;
import com.vulkantechnologies.menu.configuration.adapter.compact.CompactIntegerAdapter;
import com.vulkantechnologies.menu.configuration.adapter.compact.CompactStringAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VariableUtils {

    public static CompactAdapter<?> findAdapter(String value) {
        if (isInteger(value))
            return CompactIntegerAdapter.INSTANCE;
        else if (isDouble(value))
            return CompactDoubleAdapter.INSTANCE;
        else if (isBoolean(value))
            return CompactBooleanAdapter.INSTANCE;
        return CompactStringAdapter.INSTANCE;
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
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
