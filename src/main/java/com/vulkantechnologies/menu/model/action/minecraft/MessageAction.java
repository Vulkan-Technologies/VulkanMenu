package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;

import net.kyori.adventure.text.Component;

@ComponentName("message")
public record MessageAction(Component message) implements Action {

    @Override
    public void accept(Player player) {
        player.sendMessage(message);
    }

}
