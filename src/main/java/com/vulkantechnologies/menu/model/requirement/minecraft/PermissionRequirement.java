package com.vulkantechnologies.menu.model.requirement.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("permission")
public record PermissionRequirement(String permission) implements Requirement {

    @Override
    public boolean test(Player player) {
        return player.hasPermission(this.permission);
    }
}
