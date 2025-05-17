package com.vulkantechnologies.menu.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;

@CommandAlias("vmenu")
public class VMenuCommand extends BaseCommand {

    @Dependency
    private VulkanMenu plugin;

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage(Component.text("VulkanMenu v")
                .append(Component.text(plugin.getDescription().getVersion()))
                .append(Component.text(" by Vulkan Technologies")));
        sender.sendMessage(Component.text("Use /vmenu help for a list of commands."));
    }

    @Subcommand("open")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onOpen(Player player, MenuConfiguration menu) {
        this.plugin.menu().openMenu(player, menu);
    }

    @Subcommand("reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(Component.text("Reloading VulkanMenu..."));
        try {
            this.plugin.configuration().load();
            sender.sendMessage(Component.text("VulkanMenu reloaded.")
                    .appendSpace()
                    .append(Component.text("With " + this.plugin.configuration().menus().size() + " menus.")));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to reload VulkanMenu: " + e.getMessage()));
            e.printStackTrace();
        }
    }
}
