package com.vulkantechnologies.menu.configuration;

import java.util.Map;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.WrappedRequirement;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public record MenuConfiguration(Component title, int size, @Nullable CommandConfiguration openCommand,
                                Map<String, MenuItem> items,
                                @Nullable Map<String, Action> openActions, @Nullable Map<String, Action> closeActions,
                                @Nullable Map<String, WrappedRequirement> openRequirements) {

    public boolean validate(VulkanMenu plugin) {
        // Size validation
        if (this.size % 9 != 0) {
            plugin.getLogger().warning("Menu size must be a multiple of 9.");
            return false;
        } else if (this.size > 54) {
            plugin.getLogger().warning("Menu size must be less than or equal to 54.");
            return false;
        } else if (this.size <= 0) {
            plugin.getLogger().warning("Menu size must be greater than 0.");
            return false;
        }

        // Title validation
        if (this.title == null) {
            plugin.getLogger().warning("Menu title cannot be null.");
            return false;
        } else if (this.title.toString().isEmpty()) {
            plugin.getLogger().warning("Menu title cannot be empty.");
            return false;
        }

        // Open command validation
        if (this.openCommand != null) {
            if (this.openCommand.name() == null || this.openCommand.name().isEmpty()) {
                plugin.getLogger().warning("Menu open command name cannot be null or empty.");
                return false;
            }
        }

        return true;
    }

    public boolean canOpen(Player player) {
        if (this.openRequirements == null || this.openRequirements.isEmpty())
            return true;

        return this.openRequirements()
                .values()
                .stream()
                .allMatch(requirement -> requirement.test(player));
    }
}
