package com.vulkantechnologies.menu.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.DefaultConfigurationFile;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.configuration.MenuConfigurationFile;
import com.vulkantechnologies.menu.configuration.importer.DeluxeMenuImporter;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;

public class ImportService {

    private final VulkanMenu plugin;
    private final List<ConfigurationImporter> importers = new ArrayList<>();

    public ImportService(VulkanMenu plugin) {
        this.plugin = plugin;

        this.importers.add(new DeluxeMenuImporter(this.plugin));
    }

    public void importMenus(ConfigurationImporter importer) {
        Path menuFolder = importer.menusFolder();
        if (!Files.isDirectory(menuFolder)) {
            this.plugin.getLogger().warning("The menu folder " + menuFolder + " does not exist or is not a directory.");
            return;
        }

        this.plugin.getLogger().info("Importing menus from " + menuFolder);
        Path dataFolder = this.plugin.getDataPath().resolve(importer.dataFolder());
        if (!Files.isDirectory(dataFolder)) {
            try {
                Files.createDirectories(dataFolder);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create data folder " + dataFolder, e);
            }
        }

        try (Stream<Path> pathStream = Files.walk(menuFolder)) {
            pathStream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(path -> {
                        // Load
                        DefaultConfigurationFile configurationFile = new DefaultConfigurationFile(path);
                        configurationFile.load();

                        // Import
                        MenuConfiguration menu = importer.process(configurationFile.root());

                        // Export
                        MenuConfigurationFile menuConfigurationFile = new MenuConfigurationFile(dataFolder.resolve(path.getFileName()));
                        menuConfigurationFile.menu(menu);
                        menuConfigurationFile.save();

                        // Load new menu
                        String id = path.getFileName().toString().replace(".yml", "");
                        this.plugin.configuration().register(id, menuConfigurationFile);

                        this.plugin.getLogger().info("Imported " + menuFolder + " to " + dataFolder);
                    });

            this.plugin.getLogger().info("Imported " + menuFolder + " to " + dataFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import menus from " + menuFolder, e);
        }
    }

    public Optional<ConfigurationImporter> findByPluginName(String pluginName) {
        return this.importers.stream()
                .filter(importer -> importer.pluginName().equalsIgnoreCase(pluginName))
                .findFirst();
    }

    @Unmodifiable
    public List<ConfigurationImporter> availableImporters() {
        return this.importers.stream()
                .filter(importer -> this.plugin.getServer().getPluginManager().isPluginEnabled(importer.pluginName()))
                .toList();
    }

    @Unmodifiable
    public List<ConfigurationImporter> importers() {
        return List.copyOf(importers);
    }
}
