package com.vulkantechnologies.menu;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.vulkantechnologies.menu.command.VMenuCommand;
import com.vulkantechnologies.menu.command.completion.MenuCompletionHandler;
import com.vulkantechnologies.menu.command.context.MenuContextResolver;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.listener.InventoryListener;
import com.vulkantechnologies.menu.service.ConfigurationService;
import com.vulkantechnologies.menu.service.MenuService;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;

@Getter
public final class VulkanMenu extends JavaPlugin {

    // Configuration
    private ConfigurationService configuration;

    // Service
    private MenuService menu;

    // Commands
    private PaperCommandManager commands;

    @Override
    public void onEnable() {
        // Configuration
        this.configuration = new ConfigurationService(this);
        this.configuration.load();

        // Manager
        this.menu = new MenuService();

        // Commands
        this.commands = new PaperCommandManager(this);
        this.commands.getCommandContexts().registerContext(MenuConfiguration.class, new MenuContextResolver(this));
        this.commands.getCommandCompletions().registerAsyncCompletion("menus", new MenuCompletionHandler(this));
        this.commands.registerCommand(new VMenuCommand());

        // Listeners
        List.of(
                new InventoryListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        // Commands
        this.commands.unregisterCommands();
    }
}
