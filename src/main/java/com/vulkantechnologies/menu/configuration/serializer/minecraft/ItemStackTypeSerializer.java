package com.vulkantechnologies.menu.configuration.serializer.minecraft;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.text.Component;

public class ItemStackTypeSerializer implements TypeSerializer<ItemStack> {

    public static final ItemStackTypeSerializer INSTANCE = new ItemStackTypeSerializer();

    @Override
    public ItemStack deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        Material material = node.node("material").get(Material.class);
        if (material == null)
            throw new SerializationException("Material is null");
        int amount = node.node("amount").getInt(1);

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        // Display name
        Component displayName = node.node("name").get(Component.class);
        if (displayName != null)
            meta.displayName(displayName);

        // Lore
        List<Component> lore = node.node("lore").getList(Component.class);
        if (lore != null)
            meta.lore(lore);

        // Enchantments
        ConfigurationNode enchantments = node.node("enchantments");
        Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
        enchantments.childrenMap().forEach((o, node1) -> {
            Enchantment enchantment;
            try {
                enchantment = node1.node("enchantment").get(Enchantment.class);
            } catch (SerializationException e) {
                throw new RuntimeException("Invalid enchantment: " + node1.node("enchantment").getString());
            }
            int level = node1.node("level").getInt();
            enchantmentMap.put(enchantment, level);
        });
        enchantmentMap.forEach((enchantment, integer) -> meta.addEnchant(enchantment, integer, true));

        // Unbreakable
        boolean unbreakable = node.node("unbreakable").getBoolean();
        meta.setUnbreakable(unbreakable);

        // Max stack size
        int maxStackSize = node.node("max-stack-size").getInt(-1);
        if (maxStackSize > 0)
            meta.setMaxStackSize(maxStackSize);

        // Hide tooltip
        boolean hideTooltip = node.node("hide-tooltip").getBoolean();
        meta.setHideTooltip(hideTooltip);

        // Custom model data
        int customModelData = node.node("custom-model-data").getInt();
        meta.setCustomModelData(customModelData);

        // Item flags
        List<ItemFlag> itemFlags = node.node("item-flags").getList(ItemFlag.class);
        if (itemFlags != null)
            itemFlags.forEach(meta::addItemFlags);

        // Attributes
        ConfigurationNode attributes = node.node("attributes");
        attributes.childrenMap().forEach((o, node1) -> {
            try {
                Attribute attribute = node1.node("attribute").get(Attribute.class);
                AttributeModifier modifier = node1.node("modifier").get(AttributeModifier.class);

                meta.addAttributeModifier(attribute, modifier);
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        });

        // Version
        int version = node.node("version").getInt();
        meta.setVersion(version);

        // Enchantments glint override
        boolean enchantmentGlintOverride = node.node("enchantment-glint-override").getBoolean();
        meta.setEnchantmentGlintOverride(enchantmentGlintOverride);

        // PDC
        // TODO: Implement PDC

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemStack obj, @NotNull ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
