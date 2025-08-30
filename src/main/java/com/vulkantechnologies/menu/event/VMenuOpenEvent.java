package com.vulkantechnologies.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.vulkantechnologies.menu.model.menu.Menu;

public class VMenuOpenEvent extends VMenuEvent implements Cancellable {

    private boolean cancelled;

    public VMenuOpenEvent(Player player, Menu menu) {
        super(player, menu);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
