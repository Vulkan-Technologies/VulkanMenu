package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

import net.kyori.adventure.key.Key;

public class CompactKeyAdapter implements CompactAdapter<Key> {

    public static final CompactKeyAdapter INSTANCE = new CompactKeyAdapter();

    @Override
    public Key deserialize(CompactContext context) {
        String key = context.popFirstArg();

        return Key.key(key);
    }

    @Override
    public String serialize(Key object) {
        return object.asString();
    }

    @Override
    public Class<Key> type() {
        return Key.class;
    }
}
