package com.vulkantechnologies.menu.configuration.serializer.adventure;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ComponentTypeSerializer implements TypeSerializer<Component> {

    public static final ComponentTypeSerializer INSTANCE = new ComponentTypeSerializer();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public Component deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return Component.empty();

        String value = node.getString();
        if (value == null)
            throw new SerializationException("Component cannot be null");

        return MINI_MESSAGE.deserialize(value);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Component obj, @NotNull ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Serialization is not supported");
    }
}
