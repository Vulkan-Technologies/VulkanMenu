package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.CompactAdapter;

public class CompactIntegerAdapter implements CompactAdapter<Integer> {

    @Override
    public Integer adapt(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value, e);
        }
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }
}
