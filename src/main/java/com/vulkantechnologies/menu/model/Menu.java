package com.vulkantechnologies.menu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;

import lombok.Data;

@Data
public class Menu implements InventoryHolder {

    private final UUID uniqueId;
    private final MenuConfiguration configuration;
    private final Inventory inventory;
    private final List<MenuItem> items;

    public Menu(MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.configuration = configuration;
        this.inventory = Bukkit.createInventory(null, configuration.size(), configuration.title());
        this.items = new ArrayList<>(configuration.items().values());

        this.build();
    }

    private void build() {
        for (MenuItem item : this.items) {
            this.inventory.setItem(item.slot(), item.item());
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
