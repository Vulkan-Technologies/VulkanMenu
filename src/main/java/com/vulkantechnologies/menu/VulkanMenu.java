package com.vulkantechnologies.menu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.vulkantechnologies.menu.command.LiveConfigurationCommand;
import com.vulkantechnologies.menu.command.VMenuCommand;
import com.vulkantechnologies.menu.command.completion.MenuCompletionHandler;
import com.vulkantechnologies.menu.command.context.MenuContextResolver;
import com.vulkantechnologies.menu.configuration.MainConfiguration;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.listener.InventoryListener;
import com.vulkantechnologies.menu.listener.MarkerListener;
import com.vulkantechnologies.menu.listener.UpdateListener;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.service.*;
import com.vulkantechnologies.menu.task.MenuRefreshTask;

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
    private MainConfiguration mainConfiguration;

    // Placeholders
    private final Set<PlaceholderProcessor> placeholderProcessors = new HashSet<>();

    // Service
    private PluginHookService pluginHooks;
    private MenuService menu;
    private FileWatcherService fileWatcher;
    private UpdateService updates;

    // Task
    private MenuRefreshTask refreshTask;

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

        // Main configuration
        this.mainConfiguration = new MainConfiguration(this.getDataFolder().toPath().resolve("config.yml"));
        this.mainConfiguration.load();

        // Services
        this.pluginHooks = new PluginHookService(this);
        this.menu = new MenuService(this);
        this.fileWatcher = new FileWatcherService(this);
        this.updates = new UpdateService(this);

        // Commands
        this.commands = new PaperCommandManager(this);
        this.commands.enableUnstableAPI("help");
        this.commands.getCommandContexts().registerContext(MenuConfiguration.class, new MenuContextResolver(this));
        this.commands.getCommandCompletions().registerAsyncCompletion("menus", new MenuCompletionHandler(this));
        this.commands.registerCommand(new VMenuCommand());
        this.commands.registerCommand(new LiveConfigurationCommand());

        // Listeners
        List.of(
                new InventoryListener(this),
                new MarkerListener(this),
                new UpdateListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        // Hooks
        this.pluginHooks.check();
        Bukkit.getScheduler().runTask(this, () -> this.pluginHooks.retry());

        // Tasks
        this.refreshTask = new MenuRefreshTask(this);
        this.refreshTask.runTaskTimer(this, 0L, 1L);

        // Metrics
        this.metrics = new Metrics(this, 25916);
        this.metrics.addCustomChart(new SingleLineChart("menu_count", () -> this.menu.menus().size()));

        // Update checker
        Bukkit.getScheduler().runTaskLater(this, () -> this.updates.checkForUpdates(), 20L);

        // API
        VMenuAPI.init(this);

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

        // Services
        this.fileWatcher.stop();

        // Tasks
        this.refreshTask.cancel();

        this.disabled = true;
    }

    public String processPlaceholders(Player player, Menu menu, String text) {
        return menu.injectVariable(processPlaceholders(player, text));
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
