package com.vulkantechnologies.menu.model;

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

    public Menu(MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.configuration = configuration;
        this.inventory = Bukkit.createInventory(null, configuration.size(), configuration.title());
    }


    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
