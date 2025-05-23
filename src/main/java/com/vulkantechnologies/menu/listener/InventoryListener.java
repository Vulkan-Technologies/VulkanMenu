package com.vulkantechnologies.menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryListener implements Listener {

    private final VulkanMenu plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)
            || e.getClickedInventory() == null
            || !(e.getInventory().getHolder(false) instanceof Menu menu))
            return;

        e.setCancelled(true);

        int slot = e.getRawSlot();
        menu.getItem(slot)
                .ifPresent(item -> item.handleClick(player, menu, e.getClick()));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)
            || !(e.getInventory().getHolder() instanceof Menu menu))
            return;

        this.plugin.menu().closeMenu(player, menu);
    }
}
