package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("console")
public record ConsoleCommandAction(String command) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        String formattedCommand = VulkanMenu.get().processPlaceholders(player, command);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), menu.injectVariable(formattedCommand));
    }
}
