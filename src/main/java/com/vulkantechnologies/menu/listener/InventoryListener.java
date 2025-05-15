package com.vulkantechnologies.menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.Menu;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryListener implements Listener {

    private final VulkanMenu plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)
            || !(e.getInventory().getHolder() instanceof Menu menu))
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)
            || !(e.getInventory().getHolder() instanceof Menu menu))
            return;


    }
}
