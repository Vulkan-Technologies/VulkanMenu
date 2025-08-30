package com.vulkantechnologies.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.Getter;

@Getter
public abstract class VMenuEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Menu menu;

    public VMenuEvent(Player player, Menu menu) {
        this.menu = menu;
        this.player = player;
    }

    public VMenuEvent(Player player, boolean isAsync, Menu menu) {
        super(isAsync);
        this.player = player;
        this.menu = menu;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
