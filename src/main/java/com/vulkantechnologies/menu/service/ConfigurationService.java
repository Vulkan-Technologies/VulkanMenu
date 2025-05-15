package com.vulkantechnologies.menu.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfigurationFile;
import com.vulkantechnologies.menu.exception.MenuConfigurationLoadException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationService {

    private final VulkanMenu plugin;
    private final Map<String, MenuConfigurationFile> menus = new HashMap<>();

    public void load() {
        Path dataFolder = this.plugin.getDataPath();
        this.createDataFolder();

        this.plugin.getSLF4JLogger().info("Loading menus...");

        try (Stream<Path> pathStream = Files.walk(dataFolder)) {
            pathStream.filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> {
                        try {
                            MenuConfigurationFile file = this.load(path);
                            String id = path.getFileName().toString().replace(".yml", "");
                            this.menus.put(id, file);
                        } catch (MenuConfigurationLoadException e) {
                            this.plugin.getSLF4JLogger().error("Failed to load menu configuration: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            this.plugin.getSLF4JLogger().error("Failed to load menus", e);
        }
    }

    public MenuConfigurationFile load(Path path) throws MenuConfigurationLoadException {
        if (!Files.exists(path))
            throw new MenuConfigurationLoadException("Configuration file does not exist");

        MenuConfigurationFile file = new MenuConfigurationFile(path);
        try {
            file.load();
        } catch (Exception e) {
            throw new MenuConfigurationLoadException("Failed to load configuration", e);
        }
        return file;
    }

    public Optional<MenuConfigurationFile> findByName(String id) {
        return Optional.ofNullable(this.menus.get(id));
    }

    public void unregister(String id) {
        this.menus.remove(id);
    }

    private void createDataFolder() {
        Path dataFolder = this.plugin.getDataPath();
        if (Files.exists(dataFolder))
            return;

        try {
            Files.createDirectories(dataFolder);

            this.saveResource("configuration/default.yml", dataFolder.resolve("default.yml"));
        } catch (IOException e) {
            this.plugin.getSLF4JLogger().error("Failed to create data folder", e);
        }
    }

    private void saveResource(String resourcePath, Path path) {
        if (Files.exists(path))
            return;

        try (InputStream inputStream = this.plugin.getResource(resourcePath)) {
            if (inputStream == null) {
                this.plugin.getSLF4JLogger().error("Resource not found: {}", resourcePath);
                return;
            }
            Files.copy(inputStream, path);
        } catch (IOException e) {
            this.plugin.getSLF4JLogger().error("Failed to save resource: {}", resourcePath, e);
        }
    }

    @Unmodifiable
    public Map<String, MenuConfigurationFile> menus() {
        return Map.copyOf(this.menus);
    }


}
