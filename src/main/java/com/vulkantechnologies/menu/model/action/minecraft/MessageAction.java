package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.minimessage.MiniMessage;

@ComponentName("message")
public record MessageAction(String message) implements Action {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void accept(Player player, Menu menu) {
        player.sendMessage(MINI_MESSAGE.deserialize(message, menu.variableResolver()));
    }

}
