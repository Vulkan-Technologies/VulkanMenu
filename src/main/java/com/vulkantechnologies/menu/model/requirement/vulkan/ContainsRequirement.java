package com.vulkantechnologies.menu.model.requirement.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("contains")
public record ContainsRequirement(@Single String value, String raw) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        String processedRaw = VulkanMenu.get().processPlaceholders(player, raw);
        String processedValue = VulkanMenu.get().processPlaceholders(player, value);

        return processedRaw.contains(processedValue);
    }
}
