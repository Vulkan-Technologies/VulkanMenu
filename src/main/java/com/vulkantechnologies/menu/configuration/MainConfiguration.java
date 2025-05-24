package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;

import lombok.Getter;

@Getter
public class MainConfiguration extends ConfigurationFile {

    private UpdateChecker updateChecker;

    public MainConfiguration(Path path) {
        super(path);
    }

    @Override
    public void load(CommentedConfigurationNode root) {
        try {
            this.updateChecker = root.node("update-checker").get(UpdateChecker.class);
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to load main configuration", e);
        }
    }

    @Override
    public void save(CommentedConfigurationNode root) {

    }

    @ConfigSerializable
    public record UpdateChecker(boolean enabled, boolean notification) {
    }
}
