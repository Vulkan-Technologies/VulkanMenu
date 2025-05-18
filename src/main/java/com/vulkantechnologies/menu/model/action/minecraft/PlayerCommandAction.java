package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("player")
public record PlayerCommandAction(String command) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        String formatted = VulkanMenu.get().processPlaceholders(player, command);
        player.performCommand(menu.injectVariable(formatted));
    }

}
