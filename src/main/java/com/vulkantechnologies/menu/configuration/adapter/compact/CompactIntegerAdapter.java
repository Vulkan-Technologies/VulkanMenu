package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

public class CompactIntegerAdapter implements CompactAdapter<Integer> {

    public static final CompactIntegerAdapter INSTANCE = new CompactIntegerAdapter();

    @Override
    public Integer deserialize(CompactContext context) {
        String raw = context.popFirstArg();

        try {
            return Integer.valueOf(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + raw, e);
        }
    }

    @Override
    public String serialize(Integer object) {
        return object.toString();
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }
}
