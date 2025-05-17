package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.CompactAdapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CompactComponentAdapter implements CompactAdapter<Component> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public Component adapt(String value) {
        return MINI_MESSAGE.deserialize(value);
    }

    @Override
    public Class<Component> type() {
        return Component.class;
    }
}
