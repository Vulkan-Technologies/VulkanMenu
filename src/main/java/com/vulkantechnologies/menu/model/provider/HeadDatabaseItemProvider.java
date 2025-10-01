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
        ItemStack itemStack = this.api.getItemHead(value);
        if (itemStack == null)
            throw new RuntimeException("HeadDatabase item not found: " + value);
        return itemStack;
    }

    @Override
    public String prefix() {
        return "hdb";
    }
}
