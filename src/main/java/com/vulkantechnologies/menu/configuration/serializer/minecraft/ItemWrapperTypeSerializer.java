package com.vulkantechnologies.menu.configuration.serializer.minecraft;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vulkantechnologies.menu.utils.Version;
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

import com.vulkantechnologies.menu.model.provider.ItemStackProvider;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.registry.Registries;

public class ItemWrapperTypeSerializer implements TypeSerializer<ItemWrapper> {

    public static final ItemWrapperTypeSerializer INSTANCE = new ItemWrapperTypeSerializer();

    @Override
    public ItemWrapper deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        ItemStack item;

        ConfigurationNode materialNode = node.node("material");
        String rawMaterial = materialNode.getString();
        if (rawMaterial.contains(":")) {
            String[] materialParts = rawMaterial.split(":");
            ItemStackProvider provider = Registries.ITEM_PROVIDERS.findByPrefix(materialParts[0])
                    .orElseThrow(() -> new SerializationException("Invalid item provider: " + materialParts[0]));
            item = provider.provide(materialParts[1]);
        } else {
            Material material = materialNode.get(Material.class);
            if (material == null)
                throw new SerializationException("Invalid material: " + rawMaterial);
            item = new ItemStack(material);
        }

        int amount = node.node("amount").getInt(1);
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        // Display name
        String displayName = node.node("name").get(String.class);
        if (displayName != null)
            meta.setDisplayName(displayName);

        // Lore
        List<String> lore = node.node("lore").getList(String.class);
        if (lore != null)
            meta.setLore(lore);

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

        if (Version.CURRENT.more(Version.V_1_20_5)) {
            // Max stack size
            int maxStackSize = node.node("max-stack-size").getInt(-1);
            if (maxStackSize > 0)
                meta.setMaxStackSize(maxStackSize);

            // Hide tooltip
            boolean hideTooltip = node.node("hide-tooltip").getBoolean();
            meta.setHideTooltip(hideTooltip);

            // Enchantments glint override
            boolean enchantmentGlintOverride = node.node("enchantment-glint-override").getBoolean();
            meta.setEnchantmentGlintOverride(enchantmentGlintOverride);
        }

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

        item.setItemMeta(meta);

        return new ItemWrapper(item, displayName, lore);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemWrapper obj, @NotNull ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
