package com.vulkantechnologies.menu.configuration.menu;

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
                                Map<String, String> variables, @Nullable Refresh refresh) {

    @ConfigSerializable
    public record Refresh(int interval, int delay, List<Action> actions) {

        public boolean isValid() {
            return (this.interval > 0 || this.delay > 0) && (this.actions != null && !this.actions.isEmpty());
        }

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
            plugin.getSLF4JLogger().warn("Menu size must be a multiple of 9.");
            return false;
        } else if (this.size > 54) {
            plugin.getSLF4JLogger().warn("Menu size must be less than or equal to 54.");
            return false;
        } else if (this.size <= 0) {
            plugin.getSLF4JLogger().warn("Menu size must be greater than 0.");
            return false;
        }

        // Title validation
        if (this.title == null) {
            plugin.getSLF4JLogger().warn("Menu title cannot be null.");
            return false;
        } else if (this.title.toString().isEmpty()) {
            plugin.getSLF4JLogger().warn("Menu title cannot be empty.");
            return false;
        }

        // Open command validation
        if (this.openCommand != null && this.openCommand.name() != null) {
            if (this.openCommand.name().isEmpty()) {
                plugin.getSLF4JLogger().warn("Menu open command name cannot be null or empty.");
                return false;
            }
        }

        // Variables
        if (this.variables != null)
            for (Map.Entry<String, String> entry : this.variables.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();

                if (!Key.parseable(name)) {
                    plugin.getSLF4JLogger().warn("Variable name '{}' is not a valid Key.", name);
                    this.variables.remove(name);
                } else if (value == null || value.isEmpty()) {
                    plugin.getSLF4JLogger().warn("Variable value for '{}' cannot be null or empty.", name);
                    this.variables.remove(name);
                }
            }

        return true;
    }
}
