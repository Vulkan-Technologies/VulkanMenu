package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.menu.ItemSlot;
import com.vulkantechnologies.menu.utils.VariableUtils;

public class ItemSlotTypeSerializer implements TypeSerializer<ItemSlot> {

    public static final ItemSlotTypeSerializer INSTANCE = new ItemSlotTypeSerializer();

    @Override
    public ItemSlot deserialize(@NotNull Type type, ConfigurationNode node) throws SerializationException {
        if (node.virtual())
            return null;

        if (node.isList())
            return ItemSlot.of(node.getList(Integer.class));

        String value = node.getString();
        if (value == null)
            return null;

        if (VariableUtils.isNumeric(value))
            return ItemSlot.of(Integer.parseInt(value));

        return ItemSlot.of(value);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemSlot obj, @NotNull ConfigurationNode node) throws SerializationException {

    }
}
