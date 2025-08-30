package com.vulkantechnologies.menu.command.menu;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.CommandConfiguration;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;

public class MenuCommand extends Command {

    private final VulkanMenu plugin;
    private final String id;

    public MenuCommand(VulkanMenu plugin, String id, CommandConfiguration command) {
        super(command.name());
        this.id = id;
        this.plugin = plugin;
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
        this.plugin.configuration().findByName(this.id).ifPresentOrElse((configFile) -> {
            this.plugin.menu().openMenu(player, configFile.menu());
        }, () -> {
            this.plugin.mainConfiguration().sendMessage(player, "menu-not-found", Placeholder.parsed("menu", this.id));
        });
        return true;
    }
}
