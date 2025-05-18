package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;

import lombok.Getter;

@Getter
public class DefaultConfigurationFile extends ConfigurationFile {

    private CommentedConfigurationNode root;

    public DefaultConfigurationFile(Path path) {
        super(path);
    }

    @Override
    public void load(CommentedConfigurationNode root) {
        this.root = root;
    }

    @Override
    public void save(CommentedConfigurationNode root) {
        throw new UnsupportedOperationException("Default configuration cannot be saved");
    }
}
