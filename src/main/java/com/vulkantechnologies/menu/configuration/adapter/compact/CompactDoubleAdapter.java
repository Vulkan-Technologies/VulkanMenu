package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.CompactAdapter;

public class CompactDoubleAdapter implements CompactAdapter<Double> {

    @Override
    public Double adapt(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value: " + value, e);
        }
    }

    @Override
    public Class<Double> type() {
        return Double.class;
    }
}
