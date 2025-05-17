package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

public class CompactBooleanAdapter implements CompactAdapter<Boolean> {

    public static final CompactBooleanAdapter INSTANCE = new CompactBooleanAdapter();

    @Override
    public Boolean adapt(CompactContext context) {
        String raw = context.popFirstArg();
        try {
            return Boolean.parseBoolean(raw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid boolean value: " + raw, e);
        }
    }

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

}
