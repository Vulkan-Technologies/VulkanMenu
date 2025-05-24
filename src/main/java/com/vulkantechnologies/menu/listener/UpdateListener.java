package com.vulkantechnologies.menu.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.vulkantechnologies.menu.VulkanMenu;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RequiredArgsConstructor
public class UpdateListener implements Listener {

    private final VulkanMenu plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!this.plugin.mainConfiguration().updateChecker().notification()
            || !player.hasPermission("vulkanmenu.update.notify"))
            return;

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            player.sendMessage(Component.text("VulkanMenu Update Notification", NamedTextColor.GOLD)
                    .append(Component.newline())
                    .append(Component.text("A new version of VulkanMenu is available!", NamedTextColor.RED))
                    .append(Component.newline())
                    .append(Component.text("Current Version: " + this.plugin.getDescription().getVersion(), NamedTextColor.GRAY))
                    .append(Component.newline())
                    .append(Component.text("Latest Version: " + this.plugin.updates().latestVersion(), NamedTextColor.GREEN))
                    .append(Component.newline())
                    .append(Component.text("Please update to the latest version for new features and fixes.", NamedTextColor.GRAY)));
        }, 20L);
    }
}
