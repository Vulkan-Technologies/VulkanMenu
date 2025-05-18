package com.vulkantechnologies.menu.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;

@CommandAlias("vmenu")
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

    @Subcommand("import")
    @Description("Import all menus from the specified importer.")
    @CommandCompletion("@importers")
    @Syntax("<importer>")
    @CommandPermission("vmenu.import")
    public void onImport(CommandSender sender, ConfigurationImporter importer) {
        sender.sendMessage(Component.text("Importing menus from " + importer.pluginName() + "..."));
        try {
            this.plugin.importService().importMenus(importer);
            sender.sendMessage(Component.text("Imported " + this.plugin.configuration().menus().size() + " menus."));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to import menus: " + e.getMessage()));
            e.printStackTrace();
        }
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

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
