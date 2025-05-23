package com.vulkantechnologies.menu.model.requirement.vulkan;

import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("regex")
public record RegexRequirement(String value) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        // Process placeholders
        String formattedValue = VulkanMenu.get().processPlaceholders(player, menu, value);


        Pattern pattern = Pattern.compile(formattedValue);
        return pattern.matcher(player.getName()).find();
    }

}
