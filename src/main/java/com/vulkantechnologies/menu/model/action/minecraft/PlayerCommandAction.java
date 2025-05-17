package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;

@ComponentName("player")
public record PlayerCommandAction(String command) implements Action {

    @Override
    public void accept(Player player) {
        if (command == null || command.isEmpty())
            return;

        player.performCommand(command);
    }

}
