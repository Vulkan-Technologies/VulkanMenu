package com.vulkantechnologies.menu.service;

import java.util.*;
import java.util.function.Supplier;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.hook.implementation.*;
import org.bukkit.plugin.Plugin;

public class PluginHookService {

    private final VulkanMenu plugin;
    private final Map<String, Supplier<? extends PluginHook>> suppliers = new HashMap<>();
    private final Map<String, Supplier<? extends PluginHook>> retries = new HashMap<>();
    private final Set<PluginHook> hooks = new HashSet<>();

    public PluginHookService(VulkanMenu plugin) {
        this.plugin = plugin;

        // Register hooks
        this.register("Vault", () -> new VaultPluginHook(plugin));
        this.register("PlaceholderAPI", () -> new PlaceholderAPIPluginHook(plugin));
        this.register("ItemsAdder", () -> new ItemsAdderPluginHook(plugin));
        this.register("Oraxen", () -> new OraxenPluginHook(plugin));
        this.register("Nexo", () -> new NexoPluginHook(plugin));
        this.register("packetevents", () -> new PacketEventsPluginHook(plugin));
        this.register("HeadDatabase", () -> new HeadDatabasePluginHook(plugin));
    }

    public void register(String name, Supplier<? extends PluginHook> supplier) {
        this.suppliers.put(name, supplier);
    }

    public void check() {
        for (var hook : this.suppliers.entrySet()) {
            String pluginName = hook.getKey();
            Supplier<? extends PluginHook> supplier = hook.getValue();
            if (plugin.getServer().getPluginManager().isPluginEnabled(pluginName)) {
                PluginHook pluginHook = supplier.get();
                pluginHook.onSuccess();
                this.hooks.add(pluginHook);
                continue;
            }

            retries.put(pluginName, supplier);
        }
    }

    public void retry() {
        if (retries.isEmpty())
            return;

        plugin.getSLF4JLogger().info("Post-loading hooks...");
        Set<String> loaded = new HashSet<>();
        for (var hook : this.retries.entrySet()) {
            String pluginName = hook.getKey();
            Supplier<? extends PluginHook> supplier = hook.getValue();
            if (plugin.getServer().getPluginManager().isPluginEnabled(pluginName)) {
                PluginHook pluginHook = supplier.get();
                pluginHook.onSuccess();
                this.hooks.add(pluginHook);
                loaded.add(pluginName);
                continue;
            }
            this.plugin.getSLF4JLogger().warn("Failed to load plugin hook for {} - plugin not enabled.", pluginName);
        }
        this.retries.keySet().removeAll(loaded);
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
