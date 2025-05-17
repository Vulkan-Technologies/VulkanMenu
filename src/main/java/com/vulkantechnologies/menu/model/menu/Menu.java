package com.vulkantechnologies.menu.model.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;

import lombok.Data;

@Data
public class Menu implements InventoryHolder {

    private final UUID uniqueId;
    private final Player player;
    private final MenuConfiguration configuration;
    private final Inventory inventory;
    private final List<MenuItem> items;

    public Menu(Player player, MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.player = player;
        this.configuration = configuration;
        this.inventory = Bukkit.createInventory(this, configuration.size(), configuration.title());
        this.items = new ArrayList<>(configuration.items().values());

        this.build();
    }

    private void build() {
        for (MenuItem item : this.items) {
            if (!item.shouldShow(player))
                continue;

            this.inventory.setItem(item.slot(), item.item());
        }
    }

    public Optional<MenuItem> getItem(int slot) {
        return this.items
                .stream()
                .filter(item -> item.slot() == slot)
                .findFirst();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
