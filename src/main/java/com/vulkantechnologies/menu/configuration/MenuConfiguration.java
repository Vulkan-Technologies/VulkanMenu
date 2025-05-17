package com.vulkantechnologies.menu.configuration;

import java.util.Map;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.WrappedRequirement;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public record MenuConfiguration(Component title, int size, @Nullable String openCommand, Map<String, MenuItem> items,
                                @Nullable Map<String, Action> openActions, @Nullable Map<String, Action> closeActions,
                                @Nullable Map<String, WrappedRequirement> openRequirements) {

    public boolean canOpen(Player player) {
        if (this.openRequirements == null || this.openRequirements.isEmpty())
            return true;

        return this.openRequirements()
                .values()
                .stream()
                .allMatch(requirement -> requirement.test(player));
    }
}
