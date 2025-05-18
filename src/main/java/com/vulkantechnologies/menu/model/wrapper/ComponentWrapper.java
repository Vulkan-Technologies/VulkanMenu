package com.vulkantechnologies.menu.model.wrapper;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
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

    public Component build(Player player, Menu menu) {
        String formatted = VulkanMenu.get().processPlaceholders(player, this.content);
        return MINI_MESSAGE.deserialize(formatted, menu.variableResolver());
    }
}
