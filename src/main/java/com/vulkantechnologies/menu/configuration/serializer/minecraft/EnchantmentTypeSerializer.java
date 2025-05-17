package com.vulkantechnologies.menu.configuration.serializer.minecraft;

import java.lang.reflect.Type;

import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.key.Key;

public class EnchantmentTypeSerializer implements TypeSerializer<Enchantment> {

    public static final EnchantmentTypeSerializer INSTANCE = new EnchantmentTypeSerializer();

    @Override
    public Enchantment deserialize(@NotNull Type type, ConfigurationNode node) throws SerializationException {
        if (node.isNull())
            return null;

        Key key = node.get(Key.class);
        if (key == null)
            throw new SerializationException("No enchantment key found");

        return Registry.ENCHANTMENT.getOrThrow(key);
    }

    @Override
    public void serialize(Type type, @Nullable Enchantment obj, ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}