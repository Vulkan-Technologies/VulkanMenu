package com.vulkantechnologies.menu.model.action.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;

@ComponentName("open-menu")
public record OpenCommandAction(String menuId) implements Action {

    @Override
    public void accept(Player player) {
        VulkanMenu plugin = VulkanMenu.get();

        plugin.menu().openMenu(player, menuId);
    }

}
