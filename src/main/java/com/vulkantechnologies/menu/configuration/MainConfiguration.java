package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;

import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Getter
public class MainConfiguration extends ConfigurationFile {

    private UpdateChecker updateChecker;
    private Map<String, ComponentWrapper> messages = new HashMap<>();

    public MainConfiguration(Path path) {
        super(path);
    }

    @Override
    public void load(CommentedConfigurationNode root) {
        try {
            this.updateChecker = root.node("update-checker").get(UpdateChecker.class);

            ConfigurationNode messagesNode = root.node("messages");
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : messagesNode.childrenMap().entrySet()) {
                String key = entry.getKey().toString();
                ComponentWrapper message = entry.getValue().get(ComponentWrapper.class);

                if (message != null)
                    this.messages.put(key, message);
                else
                    throw new SerializationException("Failed to deserialize message for key: " + key);
            }
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to load main configuration", e);
        }
    }

    @Override
    public void save(CommentedConfigurationNode root) {

    }

    public Optional<ComponentWrapper> message(String key) {
        return Optional.ofNullable(this.messages.get(key));
    }

    public Component messageAsComponentOrDefault(Player player, Menu menu, String key, Component defaultMessage, TagResolver... resolvers) {
        return this.message(key)
                .map(componentWrapper -> componentWrapper.build(player, menu, resolvers))
                .orElse(defaultMessage);
    }

    public Component messageAsComponentOrDefault(String key, Component defaultMessage, TagResolver... resolvers) {
        return this.message(key)
                .map(componentWrapper -> componentWrapper.build(resolvers))
                .orElse(defaultMessage);
    }

    public Component messageAsComponent(Player player, Menu menu, String key, TagResolver... resolvers) {
        return this.messageAsComponentOrDefault(player, menu, key, Component.text(key), resolvers);
    }

    public Component messageAsComponent(String key, TagResolver... resolvers) {
        return this.messageAsComponentOrDefault(key, Component.text(key), resolvers);
    }

    public void sendMessage(Player player, Menu menu, String key, TagResolver... resolvers) {
        Component component = this.messageAsComponent(player, menu, key, resolvers);
        player.sendMessage(component);
    }

    public void sendMessage(Audience audience, String key, TagResolver... resolvers) {
        Component component = this.messageAsComponent(key, resolvers);
        audience.sendMessage(component);
    }

    @ConfigSerializable
    public record UpdateChecker(boolean enabled, boolean notification) {
    }
}
