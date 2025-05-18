package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;

public class ComponentWrapperTypeSerializer implements TypeSerializer<ComponentWrapper> {

    public static final ComponentWrapperTypeSerializer INSTANCE = new ComponentWrapperTypeSerializer();

    @Override
    public ComponentWrapper deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return null;

        return new ComponentWrapper(node.getString());
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ComponentWrapper obj, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.content());
    }
}
