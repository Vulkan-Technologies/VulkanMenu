package com.vulkantechnologies.menu.model.wrapper;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public record ComponentWrapper(String content) {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component build(TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(this.content, resolvers);
    }

    public Component build(Menu menu, TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(this.content, menu.variableResolver(), TagResolver.resolver(resolvers));
    }

    public Component build(Player player, Menu menu, TagResolver... resolvers) {
        String formatted = VulkanMenu.get().processPlaceholders(player, menu, this.content);
        return MINI_MESSAGE.deserialize(formatted, menu.variableResolver(), TagResolver.resolver(resolvers));
    }


}
