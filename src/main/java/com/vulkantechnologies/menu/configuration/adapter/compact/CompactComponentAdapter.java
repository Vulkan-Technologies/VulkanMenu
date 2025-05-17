package com.vulkantechnologies.menu.configuration.adapter.compact;

import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CompactComponentAdapter implements CompactAdapter<Component> {

    public static final CompactComponentAdapter INSTANCE = new CompactComponentAdapter();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public Component adapt(CompactContext context) {
        return MINI_MESSAGE.deserialize(context.remainingArgs());
    }

    @Override
    public Class<Component> type() {
        return Component.class;
    }
}
