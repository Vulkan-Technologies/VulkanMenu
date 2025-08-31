package com.vulkantechnologies.menu.model.requirement.vulkan;

import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("regex")
public record RegexRequirement(@Single String regex, String value) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        String formattedValue = VulkanMenu.get().processPlaceholders(player, menu, value);
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(formattedValue).find();
    }

}
