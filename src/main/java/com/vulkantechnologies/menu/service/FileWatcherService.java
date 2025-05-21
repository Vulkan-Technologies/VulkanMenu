package com.vulkantechnologies.menu.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;

import co.aikar.commands.lib.expiringmap.ExpirationPolicy;
import co.aikar.commands.lib.expiringmap.ExpiringMap;

public class FileWatcherService {

    private final VulkanMenu plugin;
    private WatchService watchService;
    private WatchKey key;
    private Thread thread;

    private final ExpiringMap<Path, Boolean> fileCache = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(1, TimeUnit.SECONDS)
            .build();

    public FileWatcherService(VulkanMenu plugin) {
        this.plugin = plugin;
    }

    public void start() {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            this.key = this.plugin.getDataPath()
                    .register(
                            watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY
                    );

            this.thread = new Thread(() -> {
                while (true) {
                    try {
                        WatchKey key;
                        while ((key = watchService.take()) != null) {
                            for (WatchEvent<?> event : key.pollEvents()) {
                                Path fileName = (Path) event.context();
                                if (this.fileCache.containsKey(fileName))
                                    continue;
                                this.fileCache.put(fileName, true);
                                Path path = this.plugin.getDataPath().resolve(fileName);

                                this.plugin.configuration()
                                        .findByPath(path)
                                        .ifPresentOrElse(menuConfigurationFile -> {
                                            try {
                                                this.plugin.getSLF4JLogger().info("Reloading menu configuration: {}", path);
                                                List<Player> players = this.plugin.menu().closeAll(menuConfigurationFile.menu());

                                                if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                                                    this.plugin.configuration().unregister(menuConfigurationFile.id());
                                                    return;
                                                }

                                                this.plugin.getSLF4JLogger().info("Reloading menu configuration: {}", path);
                                                this.plugin.configuration().unregister(menuConfigurationFile.id());
                                                this.plugin.configuration().load(path);

                                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                    for (Player player : players) {
                                                        this.plugin.menu().openMenu(player, menuConfigurationFile.id());
                                                    }
                                                }, 2);
                                            } catch (Exception e) {
                                                this.plugin.getSLF4JLogger().error("Failed to reload menu configuration: {}", path, e);
                                            }
                                        }, () -> this.plugin.getSLF4JLogger().error("Failed to find menu configuration for path: {}", path));
                            }
                            key.reset();
                        }
                    } catch (InterruptedException e) {
                        this.plugin.getSLF4JLogger().error("File watcher interrupted", e);
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "VulkanMenu-FileWatcherService");
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to register watch service", e);
        }
    }

    public void stop() {
        if (this.key != null)
            this.key.cancel();

        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    public boolean isEnabled() {
        return this.thread != null
               && this.thread.isAlive();
    }

}
