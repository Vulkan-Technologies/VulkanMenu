package com.vulkantechnologies.menu.service;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.utils.UpdateChecker;

import lombok.Getter;

public class UpdateService {

    private final VulkanMenu plugin;
    @Getter
    private String latestVersion;
    @Getter
    private boolean updateAvailable = false;

    public UpdateService(VulkanMenu plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        if (!this.plugin.mainConfiguration().updateChecker().enabled())
            return;

        this.plugin.getSLF4JLogger().info("Checking for updates...");
        UpdateChecker.fetchLatestVersion()
                .whenComplete((latestVersion, throwable) -> {
                    if (throwable != null) {
                        plugin.getSLF4JLogger().warn("Failed to check for updates", throwable);
                        return;
                    }

                    this.latestVersion = latestVersion;
                    if (this.latestVersion.equals(plugin.getDescription().getVersion())) {
                        this.plugin.getSLF4JLogger().info("You are running the latest version of VulkanMenu!");
                        return;
                    }

                    this.updateAvailable = true;

                    plugin.getSLF4JLogger().info("A new version of VulkanMenu is available: v{}", this.latestVersion);
                    plugin.getSLF4JLogger().info("Please visit https://www.spigotmc.org/resources/vulkanmenu-open-source.125208/ to download the latest version.");
                });
    }
}
