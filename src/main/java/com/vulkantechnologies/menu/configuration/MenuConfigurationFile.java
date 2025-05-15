package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import lombok.Getter;

@Getter
public class MenuConfigurationFile {

    private final Path path;
    private final YamlConfigurationLoader loader;
    private MenuConfiguration menu;

    public MenuConfigurationFile(Path path) {
        this.path = path;
        this.loader = YamlConfigurationLoader.builder()
                .path(path)
                .build();
    }

    public void load() {
        try {
            CommentedConfigurationNode root = loader.load();
            this.menu = root.get(MenuConfiguration.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}
