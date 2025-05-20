package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuConfigurationFile extends ConfigurationFile {

    private String id;
    private MenuConfiguration menu;

    public MenuConfigurationFile(Path path) {
        super(path);
    }

    @Override
    public void load(CommentedConfigurationNode root) {
        try {
            this.menu = root.get(MenuConfiguration.class);
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to load menu configuration", e);
        }
    }

    @Override
    public void save(CommentedConfigurationNode root) {
        try {
            root.set(MenuConfiguration.class, this.menu);
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to save menu configuration", e);
        }
    }

}
