package com.vulkantechnologies.menu.model.importer;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;

public interface ConfigurationImporter {

    MenuConfiguration process(CommentedConfigurationNode node);

    String pluginName();

    Path dataFolder();

    Path menusFolder();
}
