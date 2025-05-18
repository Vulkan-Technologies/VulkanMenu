package com.vulkantechnologies.menu.model.wrapper;

import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public record ComponentWrapper(String content) {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component build() {
        return MINI_MESSAGE.deserialize(this.content);
    }

    public Component build(Menu menu) {
        return MINI_MESSAGE.deserialize(this.content, menu.variableResolver());
    }
}
