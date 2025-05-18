package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.minimessage.MiniMessage;

@ComponentName("message")
public record MessageAction(String message) implements Action {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void accept(Player player, Menu menu) {
        String formatted = message;
        for (PlaceholderProcessor placeholderProcessor : VulkanMenu.get().placeholderProcessors()) {
            formatted = placeholderProcessor.process(player, formatted);
        }

        player.sendMessage(MINI_MESSAGE.deserialize(formatted, menu.variableResolver()));
    }

}
