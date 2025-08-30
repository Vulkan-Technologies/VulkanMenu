package com.vulkantechnologies.menu.command;

import org.bukkit.command.CommandSender;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.service.FileWatcherService;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

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

        if (fileWatcher.isEnabled())
            fileWatcher.stop();
        else
            fileWatcher.start();

        this.plugin.mainConfiguration().sendMessage(
                sender,
                "live-reload-toggle",
                Placeholder.component("status", this.plugin.mainConfiguration().messageAsComponent(fileWatcher.isEnabled() ? "enabled" : "disabled"))
        );
    }

    @Subcommand("status")
    @Description("Shows the status of the live configuration.")
    public void onStatus(CommandSender sender) {
        FileWatcherService fileWatcher = this.plugin.fileWatcher();

        this.plugin.mainConfiguration().sendMessage(
                sender,
                "live-reload-status",
                Placeholder.component("status", this.plugin.mainConfiguration().messageAsComponent(fileWatcher.isEnabled() ? "enabled" : "disabled"))
        );
    }
}
