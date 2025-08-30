package com.vulkantechnologies.menu.event;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.menu.Menu;

public class VMenuCloseEvent extends VMenuEvent {

    public VMenuCloseEvent(Player player, Menu menu) {
        super(player, menu);
    }

    public VMenuCloseEvent(Player player, boolean isAsync, Menu menu) {
        super(player, isAsync, menu);
    }

}
