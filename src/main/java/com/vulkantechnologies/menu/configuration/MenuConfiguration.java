package com.vulkantechnologies.menu.configuration;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public record MenuConfiguration(Component title, int size, @Nullable CommandConfiguration openCommand,
                                Map<String, MenuItem> items,
                                @Nullable Map<String, Action> openActions, @Nullable Map<String, Action> closeActions,
                                @Nullable Map<String, RequirementWrapper> openRequirements) {

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
//        if (this.variables != null)
//            for (Map.Entry<String, Object> entry : this.variables.entrySet()) {
//                String name = entry.getKey();
//                Object value = entry.getValue();
//
//                if (!Key.parseable(name)) {
//                    plugin.getLogger().warning("Variable name '" + name + "' is not a valid Key.");
//                    this.variables.remove(name);
//                    continue;
//                }
//            }

        return true;
    }
}
