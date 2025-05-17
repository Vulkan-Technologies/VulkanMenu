package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("console")
public record ConsoleCommandAction(String command) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        String formattedCommand = command.replace("<player>", player.getName());

        player.performCommand(formattedCommand);
    }
}
