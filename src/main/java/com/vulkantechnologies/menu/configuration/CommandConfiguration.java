package com.vulkantechnologies.menu.configuration;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import lombok.Builder;

@Builder
@ConfigSerializable
public record CommandConfiguration(String name, @Nullable List<String> aliases, @Nullable String description, @Nullable String permission) {

    public boolean hasAliases() {
        return this.aliases != null && !this.aliases.isEmpty();
    }

    public boolean hasDescription() {
        return this.description != null && !this.description.isEmpty();
    }

    public boolean hasPermission() {
        return this.permission != null && !this.permission.isEmpty();
    }
}
