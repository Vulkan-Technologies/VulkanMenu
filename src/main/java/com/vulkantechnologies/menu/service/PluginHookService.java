package com.vulkantechnologies.menu.service;

import java.util.*;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.hook.implementation.*;

public class PluginHookService {

    private final VulkanMenu plugin;
    private final Set<PluginHook> hooks = new HashSet<>();
    private final Set<PluginHook> retries = new HashSet<>();

    public PluginHookService(VulkanMenu plugin) {
        this.plugin = plugin;

        // Register hooks
        List.of(
                new VaultPluginHook(plugin),
                new PlaceholderAPIPluginHook(plugin),
                new ItemsAdderPluginHook(plugin),
                new OraxenPluginHook(plugin),
                new NexoPluginHook(plugin),
                new PacketEventsPluginHook(plugin),
                new HeadDatabasePluginHook(plugin)
        ).forEach(this::register);
    }

    public void register(PluginHook hook) {
        this.hooks.add(hook);
    }

    public void check() {
        for (PluginHook hook : this.hooks) {
            if (plugin.getServer().getPluginManager().isPluginEnabled(hook.pluginName())) {
                hook.onSuccess();
                continue;
            }

            retries.add(hook);
        }
    }

    public void retry() {
        if (retries.isEmpty())
            return;

        plugin.getSLF4JLogger().info("Post-loading hooks...");
        Set<PluginHook> loaded = new HashSet<>();
        for (PluginHook hook : this.retries) {
            if (plugin.getServer().getPluginManager().isPluginEnabled(hook.pluginName())) {
                hook.onSuccess();
                loaded.add(hook);
                continue;
            }

            hook.onFailure();
        }

        this.retries.removeAll(loaded);
    }

    public void remove(String pluginName) {
        this.hooks.removeIf(hook -> hook.pluginName().equalsIgnoreCase(pluginName));
    }

    public Optional<PluginHook> hook(String pluginName) {
        return this.hooks.stream()
                .filter(hook -> hook.pluginName().equalsIgnoreCase(pluginName))
                .findFirst();
    }

    public <T extends PluginHook> Optional<T> hook(Class<T> hookClass) {
        return this.hooks.stream()
                .filter(hook -> hook.getClass().equals(hookClass))
                .map(hookClass::cast)
                .findFirst();
    }

    public Set<PluginHook> hooks() {
        return Collections.unmodifiableSet(this.hooks);
    }
}
