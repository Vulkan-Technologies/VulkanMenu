package com.vulkantechnologies.menu.model.requirement.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("string-lenght")
public record StringLenghtRequirement(int min, int max, String value) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        // Process placeholders
        String formattedValue = VulkanMenu.get().processPlaceholders(player, value);

        // Check if the length of the string is within the specified range
        return formattedValue.length() >= min && formattedValue.length() <= max;
    }
}
