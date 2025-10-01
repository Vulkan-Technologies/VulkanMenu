package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.supplier.ItemStackSupplier;
import com.vulkantechnologies.menu.registry.Registries;

public class ItemStackSupplierTypeSerializer implements TypeSerializer<ItemStackSupplier> {

    public static final ItemStackSupplierTypeSerializer INSTANCE = new ItemStackSupplierTypeSerializer();

    @Override
    public ItemStackSupplier deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String namespace = node.require(String.class);
        return Registries.ITEM_STACK_SUPPLIERS.findByNamespace(namespace)
                .orElseThrow(() -> new SerializationException("No ItemStackSupplier found for namespace: " + namespace));
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemStackSupplier obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.set(obj.namespace());
    }
}
