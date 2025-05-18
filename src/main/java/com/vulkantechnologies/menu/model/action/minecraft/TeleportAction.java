package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("teleport")
public record TeleportAction(Location location) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        player.teleport(location);
    }
}
