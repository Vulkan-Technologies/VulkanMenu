package com.vulkantechnologies.menu.command;

import org.bukkit.command.CommandSender;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.service.FileWatcherService;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

@CommandAlias("vmenu|vm")
@Subcommand("live")
@CommandPermission("vmenu.live")
public class LiveConfigurationCommand extends BaseCommand {

    @Dependency
    private VulkanMenu plugin;

    @Subcommand("toggle")
    @Description("Toggles the live configuration.")
    public void onToggle(CommandSender sender) {
        FileWatcherService fileWatcher = this.plugin.fileWatcher();
        if (fileWatcher.isEnabled()) {
            fileWatcher.stop();
            sender.sendMessage("Live configuration disabled.");
        } else {
            fileWatcher.start();
            sender.sendMessage("Live configuration enabled.");
        }
    }

    @Subcommand("status")
    @Description("Shows the status of the live configuration.")
    public void onStatus(CommandSender sender) {
        FileWatcherService fileWatcher = this.plugin.fileWatcher();
        if (fileWatcher.isEnabled()) {
            sender.sendMessage("Live configuration is enabled.");
        } else {
            sender.sendMessage("Live configuration is disabled.");
        }
    }
}
