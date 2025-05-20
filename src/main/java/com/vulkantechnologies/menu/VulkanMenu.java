package com.vulkantechnologies.menu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.vulkantechnologies.menu.command.VMenuCommand;
import com.vulkantechnologies.menu.command.completion.MenuCompletionHandler;
import com.vulkantechnologies.menu.command.context.MenuContextResolver;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.listener.InventoryListener;
import com.vulkantechnologies.menu.listener.MarkerListener;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;
import com.vulkantechnologies.menu.service.ConfigurationService;
import com.vulkantechnologies.menu.service.MenuService;
import com.vulkantechnologies.menu.service.PluginHookService;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;

@Getter
public final class VulkanMenu extends JavaPlugin {

    private static VulkanMenu instance;

    // State
    private boolean loaded = false;
    private boolean enabled = false;
    private boolean disabled = false;

    // Configuration
    private ConfigurationService configuration;

    // Placeholders
    private final Set<PlaceholderProcessor> placeholderProcessors = new HashSet<>();

    // Service
    private PluginHookService pluginHooks;
    private MenuService menu;

    // Commands
    private PaperCommandManager commands;

    // Metrics
    private Metrics metrics;

    @Override
    public void onLoad() {
        this.loaded = true;
    }

    @Override
    public void onEnable() {
        // Configuration
        this.configuration = new ConfigurationService(this);
        this.configuration.load();

        // Manager
        this.pluginHooks = new PluginHookService(this);
        this.menu = new MenuService(this);

        // Commands
        this.commands = new PaperCommandManager(this);
        this.commands.enableUnstableAPI("help");
        this.commands.getCommandContexts().registerContext(MenuConfiguration.class, new MenuContextResolver(this));
        this.commands.getCommandCompletions().registerAsyncCompletion("menus", new MenuCompletionHandler(this));
        this.commands.registerCommand(new VMenuCommand());

        // Listeners
        List.of(
                new InventoryListener(this),
                new MarkerListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        // Hooks
        this.pluginHooks.check();
        Bukkit.getScheduler().runTask(this, () -> this.pluginHooks.retry());

        // Metrics
        this.metrics = new Metrics(this, 25916);
        this.metrics.addCustomChart(new SingleLineChart("menu_count", () -> this.menu.menus().size()));

        instance = this;

        this.enabled = true;
    }

    @Override
    public void onDisable() {
        if (!this.enabled)
            return;

        // Metrics
        this.metrics.shutdown();

        // Configuration
        this.configuration.unregisterCommands();

        // Commands
        this.commands.unregisterCommands();

        this.disabled = true;
    }

    public String processPlaceholders(Player player, String text) {
        text = text.replace("<player>", player.getName());

        for (PlaceholderProcessor processor : this.placeholderProcessors) {
            text = processor.process(player, text);
        }
        return text;
    }

    public static VulkanMenu get() {
        return instance;
    }
}
