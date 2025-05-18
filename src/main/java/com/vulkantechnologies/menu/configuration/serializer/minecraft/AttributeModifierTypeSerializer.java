package com.vulkantechnologies.menu.configuration.serializer.minecraft;

import java.lang.reflect.Type;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.key.Key;

public class AttributeModifierTypeSerializer implements TypeSerializer<AttributeModifier> {

    public static final AttributeModifierTypeSerializer INSTANCE = new AttributeModifierTypeSerializer();

    @Override
    public AttributeModifier deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Key key = node.node("key").get(Key.class);
        double amount = node.node("amount").getDouble();
        AttributeModifier.Operation operation = node.node("operation").get(AttributeModifier.Operation.class);
        EquipmentSlotGroup slot = node.node("slot").get(EquipmentSlotGroup.class);


        return new AttributeModifier(new NamespacedKey(key.namespace(), key.value()), amount, operation, slot);
    }

    @Override
    public void serialize(Type type, @Nullable AttributeModifier obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("key").set(obj.key());
        node.node("amount").set(obj.getAmount());
        node.node("operation").set(obj.getOperation());
        node.node("slot").set(obj.getSlotGroup());
    }
}