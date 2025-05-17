package com.vulkantechnologies.menu.command.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MenuCommand extends Command {

    private final VulkanMenu plugin;
    private final MenuConfiguration configuration;

    public MenuCommand(VulkanMenu plugin, MenuConfiguration configuration) {
        super(configuration.openCommand());
        this.plugin = plugin;
        this.configuration = configuration;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be executed by a player.", NamedTextColor.RED));
            return true;
        }

        this.plugin.menu().openMenu(player, this.configuration);
        return true;
    }
}
