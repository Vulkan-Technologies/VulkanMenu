package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

public class CompactDoubleAdapter implements CompactAdapter<Double> {

    public static final CompactDoubleAdapter INSTANCE = new CompactDoubleAdapter();

    @Override
    public Double adapt(CompactContext context) {
        String raw = context.popFirstArg();

        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value: " + raw, e);
        }
    }

    @Override
    public Class<Double> type() {
        return Double.class;
    }
}
