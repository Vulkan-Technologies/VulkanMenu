package com.vulkantechnologies.menu.model.requirement.minecraft;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("is-near")
public record IsNearRequirement(Location location, double distance) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        if (!location.getWorld().equals(player.getWorld()))
            return false;

        return player.getLocation().distanceSquared(location) <= distance * distance;
    }

}
