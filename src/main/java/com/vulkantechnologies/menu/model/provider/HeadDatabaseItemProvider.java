package com.vulkantechnologies.menu.model.provider;

import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class HeadDatabaseItemProvider implements ItemStackProvider {

    private final HeadDatabaseAPI api;

    public HeadDatabaseItemProvider() {
        this.api = new HeadDatabaseAPI();
    }

    @Override
    public ItemStack provide(String value) {
        if (value == null || value.isEmpty())
            return this.api.getRandomHead();
        return this.api.getItemHead(value);
    }

    @Override
    public String prefix() {
        return "hdb";
    }
}
