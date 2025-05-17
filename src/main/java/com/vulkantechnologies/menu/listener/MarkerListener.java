package com.vulkantechnologies.menu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.utils.ItemMarker;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkerListener implements Listener {

    private final VulkanMenu plugin;

    @EventHandler
    private void onPickup(@NotNull final EntityPickupItemEvent event) {
        if (!ItemMarker.isMarked(event.getItem().getItemStack()))
            return;

        event.getItem().remove();
    }

    @EventHandler
    private void onDrop(@NotNull final PlayerDropItemEvent event) {
        if (!ItemMarker.isMarked(event.getItemDrop().getItemStack()))
            return;

        event.getItemDrop().remove();
    }

    @EventHandler
    private void onLogin(@NotNull final PlayerLoginEvent event) {
        plugin.getServer()
                .getScheduler()
                .runTaskLater(plugin, () -> {
                    for (final ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                        if (itemStack == null || !ItemMarker.isMarked(itemStack))
                            continue;

                        event.getPlayer().getInventory().remove(itemStack);
                    }
                }, 10L);
    }
}
