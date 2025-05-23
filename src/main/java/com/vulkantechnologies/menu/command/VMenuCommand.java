package com.vulkantechnologies.menu.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

@CommandAlias("vmenu|vm")
public class VMenuCommand extends BaseCommand {

    @Dependency
    private VulkanMenu plugin;

    @Default
    @Description("Shows the VulkanMenu version and help.")
    public void onDefault(CommandSender sender) {
        sender.sendMessage(Component.text("VulkanMenu v")
                .append(Component.text(plugin.getDescription().getVersion()))
                .append(Component.text(" by Vulkan Technologies")));
        sender.sendMessage(Component.text("Use /vmenu help for a list of commands."));
    }

    @Subcommand("open")
    @Description("Opens a the specified menu to the player.")
    @CommandCompletion("@players @menus")
    @Syntax("<player> <menu>")
    @CommandPermission("vmenu.open")
    public void onOpen(Player player, OnlinePlayer target, MenuConfiguration menu) {
        this.plugin.menu().openMenu(target.getPlayer(), menu);
    }

    @Subcommand("open")
    @Description("Opens a the specified menu.")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onOpen(Player player, MenuConfiguration menu) {
        this.plugin.menu().openMenu(player, menu);
    }

    @Subcommand("list")
    @Description("Lists all available menus.")
    @CommandPermission("vmenu.list")
    public void onList(CommandSender sender) {
        sender.sendMessage(Component.text("Available menus:", NamedTextColor.GRAY));
        this.plugin.configuration()
                .menus()
                .values()
                .forEach(menu -> {
                    Component hoverText = Component.text("Size: ", NamedTextColor.GRAY).append(Component.text(menu.menu().size(), NamedTextColor.AQUA))
                            .appendNewline()
                            .append(Component.text("Items: ", NamedTextColor.GRAY).append(Component.text(menu.menu().items().size(), NamedTextColor.AQUA)))
                            .appendNewline()
                            .append(Component.text("Variables: ", NamedTextColor.GRAY).append(Component.text(menu.menu().variables().size(), NamedTextColor.AQUA)))
                            .appendNewline()
                            .appendNewline()
                            .append(Component.text("Click to open this menu.", NamedTextColor.GRAY));

                    sender.sendMessage(Component.text("- ", NamedTextColor.GRAY)
                            .append(Component.text(menu.id(), NamedTextColor.AQUA)
                                    .hoverEvent(HoverEvent.showText(hoverText))
                                    .clickEvent(ClickEvent.runCommand("/vmenu open " + menu.id()))));
                });
    }

    @Subcommand("reload")
    @Description("Reloads the VulkanMenu configuration.")
    @CommandPermission("vmenu.reload")
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

    @Subcommand("reload")
    @Description("Reloads the specified menu.")
    @CommandPermission("vmenu.reload")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onReload(CommandSender sender, String menu) {
        this.plugin.configuration()
                .findByName(menu)
                .ifPresentOrElse(menuConfigurationFile -> {
                    String id = menuConfigurationFile.id();
                    sender.sendMessage(Component.text("Reloading menu " + id + "..."));
                    try {
                        this.plugin.configuration().load(menuConfigurationFile.path());
                        sender.sendMessage(Component.text("Menu " + id + " reloaded."));
                    } catch (Exception e) {
                        sender.sendMessage(Component.text("Failed to reload menu " + id + ": " + e.getMessage()));
                        e.printStackTrace();
                    }
                }, () -> sender.sendMessage(Component.text("Menu " + menu + " not found.")));
    }


    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
