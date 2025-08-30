package com.vulkantechnologies.menu.configuration;

import java.nio.file.Path;

import com.vulkantechnologies.menu.configuration.serializer.minecraft.legacy.LegacyAttributeModifierTypeSerializer;
import com.vulkantechnologies.menu.utils.Version;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.vulkantechnologies.menu.configuration.serializer.adventure.ComponentTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.adventure.KeyTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.minecraft.modern.ModernAttributeModifierTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.minecraft.EnchantmentTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.minecraft.ItemWrapperTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.minecraft.MaterialTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.vulkan.ComponentWrapperTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.vulkan.ItemSlotTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.vulkan.MenuComponentTypeSerializer;
import com.vulkantechnologies.menu.configuration.serializer.vulkan.MenuItemTypeSerializer;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.ItemSlot;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

@Getter
public abstract class ConfigurationFile {

    private final Path path;
    private final YamlConfigurationLoader loader;

    public ConfigurationFile(Path path) {
        this.path = path;
        this.loader = YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options -> options.serializers(builder -> {
                    builder
                            .register(Component.class, ComponentTypeSerializer.INSTANCE)
                            .register(Key.class, KeyTypeSerializer.INSTANCE)
                            .register(AttributeModifier.class,
                                    Version.CURRENT.more(Version.V_1_20_5)
                                            ? ModernAttributeModifierTypeSerializer.INSTANCE
                                            : LegacyAttributeModifierTypeSerializer.INSTANCE)
                            .register(ItemWrapper.class, ItemWrapperTypeSerializer.INSTANCE)
                            .register(Action.class,
                                    new MenuComponentTypeSerializer<>(Registries.ACTION, Registries.ACTION_ADAPTER))
                            .register(Requirement.class,
                                    new MenuComponentTypeSerializer<>(Registries.REQUIREMENT, Registries.REQUIREMENT_ADAPTER))
                            .register(ComponentWrapper.class, ComponentWrapperTypeSerializer.INSTANCE)
                            .register(MenuItem.class, MenuItemTypeSerializer.INSTANCE)
                            .register(ItemSlot.class, ItemSlotTypeSerializer.INSTANCE);

                    if (Version.CURRENT.more(Version.V_1_20_5)) {
                        builder
                                .register(Material.class, MaterialTypeSerializer.INSTANCE)
                                .register(Enchantment.class, EnchantmentTypeSerializer.INSTANCE);
                    }
                }))
                .build();
    }

    public void load() {
        try {
            CommentedConfigurationNode root = loader.load();
            this.load(root);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public abstract void load(CommentedConfigurationNode root);

    public abstract void save(CommentedConfigurationNode root);

    public void save() {
        try {
            CommentedConfigurationNode root = loader.createNode();
            this.save(root);
            loader.save(root);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save configuration", e);
        }
    }
}
