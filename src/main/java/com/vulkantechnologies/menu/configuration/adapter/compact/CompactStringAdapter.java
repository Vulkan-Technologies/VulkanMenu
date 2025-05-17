package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.CompactAdapter;

public class CompactStringAdapter implements CompactAdapter<String> {
    @Override
    public String adapt(String value) {
        return value;
    }

    @Override
    public Class<String> type() {
        return String.class;
    }
}
