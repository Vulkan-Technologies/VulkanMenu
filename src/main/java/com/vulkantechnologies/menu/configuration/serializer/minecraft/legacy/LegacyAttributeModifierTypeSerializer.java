package com.vulkantechnologies.menu.configuration.serializer.minecraft.legacy;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class LegacyAttributeModifierTypeSerializer implements TypeSerializer<AttributeModifier> {

    public static final LegacyAttributeModifierTypeSerializer INSTANCE = new LegacyAttributeModifierTypeSerializer();

    @Override
    public AttributeModifier deserialize(Type type, ConfigurationNode node) throws SerializationException {
        UUID uuid = node.node("key").get(UUID.class);
        String name = node.node("name").getString();
        double amount = node.node("amount").getDouble();
        AttributeModifier.Operation operation = node.node("operation").get(AttributeModifier.Operation.class);
        EquipmentSlot slot = node.node("slot").get(EquipmentSlot.class);


        return new AttributeModifier(uuid, name, amount, operation, slot);
    }

    @Override
    public void serialize(Type type, @Nullable AttributeModifier obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("uuid").set(obj.getUniqueId());
        node.node("name").set(obj.getName());
        node.node("amount").set(obj.getAmount());
        node.node("operation").set(obj.getOperation());
        node.node("slot").set(obj.getSlot());
    }
}