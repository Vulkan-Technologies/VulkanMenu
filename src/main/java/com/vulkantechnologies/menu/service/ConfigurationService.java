package com.vulkantechnologies.menu.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.command.menu.MenuCommand;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
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
        this.menus.clear();

        // Unregister commands
        this.unregisterCommands();

        try (Stream<Path> pathStream = Files.walk(dataFolder)) {
            pathStream.filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> {
                        try {
                            this.load(path);
                        } catch (MenuConfigurationLoadException e) {
                            this.plugin.getSLF4JLogger().error("Failed to load menu configuration: {}", path, e);
                        }
                    });
            this.plugin.getSLF4JLogger().info("Loaded {} menus", this.menus.size());
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

            // Validate configuration
            if (file.menu() == null
                || !file.menu().validate(this.plugin))
                throw new MenuConfigurationLoadException("Invalid menu configuration");

            // Register menu
            String id = path.getFileName().toString().replace(".yml", "");
            file.id(id);
            this.menus.put(id, file);

            // Register command
            this.registerCommand(file.menu());
        } catch (Exception e) {
            throw new MenuConfigurationLoadException("Failed to load configuration", e);
        }
        return file;
    }

    public void registerCommand(MenuConfiguration configuration) {
        if (configuration.openCommand() == null)
            return;

        CommandMap commandMap = Bukkit.getCommandMap();
        String command = configuration.openCommand().name();
        if (command == null
            || command.isEmpty()
            || commandMap.getCommand(command) != null)
            return;

        MenuCommand cmd = new MenuCommand(this.plugin, configuration);
        commandMap.register("vulkanmenu", cmd);

        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        if (!knownCommands.containsKey(command))
            knownCommands.put(command, cmd);
    }

    public void unregisterCommands(MenuConfiguration configuration) {
        if (configuration.openCommand() == null)
            return;

        CommandMap commandMap = Bukkit.getCommandMap();
        String command = configuration.openCommand().name();
        if (command == null || command.isEmpty())
            return;

        Command cmd = commandMap.getCommand(command);
        if (cmd != null)
            cmd.unregister(commandMap);

        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        knownCommands.remove(command);
        knownCommands.remove("vulkanmenu:" + command);
    }

    public void unregisterCommands() {
        for (MenuConfigurationFile value : this.menus.values()) {
            this.unregisterCommands(value.menu());
        }
    }

    public Optional<MenuConfigurationFile> findByName(String id) {
        return Optional.ofNullable(this.menus.get(id));
    }

    public void unregister(String id) {
        MenuConfigurationFile file = this.menus.remove(id);
        if (file == null)
            return;

        this.unregisterCommands(file.menu());
    }

    public Optional<MenuConfigurationFile> findByPath(Path path) {
        return this.menus.values()
                .stream()
                .filter(menu -> menu.path().equals(path))
                .findFirst();
    }

    private void createDataFolder() {
        Path dataFolder = this.plugin.getDataPath();
        if (Files.exists(dataFolder))
            return;

        try {
            Files.createDirectories(dataFolder);

            this.saveResource("configuration/default.yml", dataFolder.resolve("default.yml"));
            this.saveResource("configuration/variables.yml", dataFolder.resolve("variables.yml"));
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
