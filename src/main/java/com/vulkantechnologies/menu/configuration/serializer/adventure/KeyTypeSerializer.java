package com.vulkantechnologies.menu.configuration.serializer.adventure;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.key.Key;

public class KeyTypeSerializer implements TypeSerializer<Key> {

    public static final KeyTypeSerializer INSTANCE = new KeyTypeSerializer();

    @Override
    public Key deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return null;

        String value = node.getString();
        if (value == null)
            throw new SerializationException("Key cannot be null");

        return Key.key(value);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Key obj, @NotNull ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
