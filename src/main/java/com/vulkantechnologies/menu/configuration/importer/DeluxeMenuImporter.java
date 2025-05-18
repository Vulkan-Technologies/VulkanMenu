package com.vulkantechnologies.menu.configuration.importer;

import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.CommandConfiguration;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeluxeMenuImporter implements ConfigurationImporter {

    private final VulkanMenu plugin;

    @Override
    public MenuConfiguration process(CommentedConfigurationNode node) {
        MenuConfiguration.MenuConfigurationBuilder builder = MenuConfiguration.builder();

        // Size
        CommentedConfigurationNode sizeNode = node.node("size");
        if (sizeNode.virtual())
            throw new IllegalArgumentException("Size is required");
        builder.size(sizeNode.getInt());

        // Title
        CommentedConfigurationNode titleNode = node.node("menu_title");
        if (titleNode.virtual())
            throw new IllegalArgumentException("Title is required");
        try {
            builder.title(titleNode.get(ComponentWrapper.class));
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to parse title", e);
        }

        // Open command
        CommentedConfigurationNode openCommandNode = node.node("open_command");
        if (!openCommandNode.virtual()) {
            CommandConfiguration.CommandConfigurationBuilder commandBuilder = CommandConfiguration.builder();
            String openCommand = openCommandNode.getString();
            if (openCommand != null && !openCommand.isEmpty())
                commandBuilder.name(openCommand);
            builder.openCommand(commandBuilder.build());
        }



        return builder.build();
    }

    @Override
    public String pluginName() {
        return "DeluxeMenus";
    }

    @Override
    public Path dataFolder() {
        return this.plugin.getDataPath()
                .getParent()
                .resolve(this.pluginName());
    }

    @Override
    public Path menusFolder() {
        return this.dataFolder()
                .resolve("gui_menus");
    }
}
