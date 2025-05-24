package com.vulkantechnologies.menu.command.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.CommandConfiguration;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;

public class MenuCommand extends Command {

    private final VulkanMenu plugin;
    private final MenuConfiguration configuration;

    public MenuCommand(VulkanMenu plugin, MenuConfiguration configuration) {
        super(configuration.openCommand().name());
        this.plugin = plugin;
        this.configuration = configuration;

        CommandConfiguration command = configuration.openCommand();
        if (command.hasAliases())
            this.setAliases(command.aliases());
        if (command.hasDescription())
            this.setDescription(command.description());
        if (command.hasPermission())
            this.setPermission(command.permission());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            this.plugin.mainConfiguration().sendMessage(sender, "player-only-command");
            return true;
        }

        this.plugin.menu().openMenu(player, this.configuration);
        return true;
    }
}
