package com.vulkantechnologies.menu.configuration;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

import lombok.Builder;
import net.kyori.adventure.key.Key;

@Builder
@ConfigSerializable
public record MenuConfiguration(ComponentWrapper title, int size, @Nullable CommandConfiguration openCommand,
                                Map<String, MenuItem> items,
                                @Nullable List<Action> openActions, @Nullable List<Action> closeActions,
                                @Nullable Map<String, RequirementWrapper> openRequirements,
                                Map<String, String> variables, Refresh refresh) {

    @ConfigSerializable
    public record Refresh(int interval, int delay, List<Action> actions) {

        public boolean hasInterval() {
            return this.interval > 0;
        }

        public boolean hasDelay() {
            return this.delay > 0;
        }

    }

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

        // Variables
        if (this.variables != null)
            for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();

                if (!Key.parseable(name)) {
                    plugin.getLogger().warning("Variable name '" + name + "' is not a valid Key.");
                    this.variables.remove(name);
                    continue;
                } else if (value == null || value.isEmpty()) {
                    plugin.getLogger().warning("Variable value for '" + name + "' cannot be null or empty.");
                    this.variables.remove(name);
                    continue;
                }
            }

        return true;
    }
}
