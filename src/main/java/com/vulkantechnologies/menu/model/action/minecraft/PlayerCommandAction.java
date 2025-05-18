package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("player")
public record PlayerCommandAction(String command) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        if (command == null || command.isEmpty())
            return;

        String formatted = command;
        for (PlaceholderProcessor placeholderProcessor : VulkanMenu.get().placeholderProcessors()) {
            formatted = placeholderProcessor.process(player, formatted);
        }

        player.performCommand(menu.injectVariable(formatted));
    }

}
