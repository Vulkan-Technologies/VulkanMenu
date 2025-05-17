package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.CompactAdapter;

public class CompactBooleanAdapter implements CompactAdapter<Boolean> {

    @Override
    public Boolean adapt(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid boolean value: " + value, e);
        }
    }

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

}
