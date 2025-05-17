package com.vulkantechnologies.menu.configuration;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.MenuItem;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public record MenuConfiguration(Component title, int size, @Nullable String openCommand, Map<String, MenuItem> items) {
}
