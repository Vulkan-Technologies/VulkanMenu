package com.vulkantechnologies.menu.configuration.serializer.minecraft;

import java.lang.reflect.Type;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.key.Key;

public class MaterialTypeSerializer implements TypeSerializer<Material> {

    public static final MaterialTypeSerializer INSTANCE = new MaterialTypeSerializer();

    @Override
    public Material deserialize(@NotNull Type type, ConfigurationNode node) throws SerializationException {
        Key materialName = node.get(Key.class);
        if (materialName == null)
            throw new SerializationException("No material key found");

        System.out.println("Material name: " + materialName);
        return Registry.MATERIAL.getOrThrow(materialName);
    }

    @Override
    public void serialize(Type type, @Nullable Material obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.name());
    }
}