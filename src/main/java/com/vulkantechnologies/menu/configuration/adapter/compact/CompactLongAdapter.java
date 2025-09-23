package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

public class CompactLongAdapter implements CompactAdapter<Long> {

    public static final CompactLongAdapter INSTANCE = new CompactLongAdapter();

    @Override
    public Long adapt(CompactContext context) {
        String raw = context.popFirstArg();

        try {
            return Long.valueOf(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid long value: " + raw, e);
        }
    }

    @Override
    public Class<Long> type() {
        return Long.class;
    }
}
