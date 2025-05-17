package com.vulkantechnologies.menu.model.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.variable.MenuVariable;

import lombok.Data;

@Data
public class Menu implements InventoryHolder {

    private final UUID uniqueId;
    private final Player player;
    private final MenuConfiguration configuration;
    private final Inventory inventory;
    private final List<MenuItem> items;
    private final List<MenuVariable<?>> variables;

    public Menu(Player player, MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.player = player;
        this.configuration = configuration;
        this.variables = new CopyOnWriteArrayList<>();
        this.inventory = Bukkit.createInventory(this, configuration.size(), configuration.title());
        this.items = new ArrayList<>(configuration.items().values());

        this.build();
    }

    public boolean canOpen(Player player) {
        if (this.configuration.openRequirements() == null || this.configuration.openRequirements().isEmpty())
            return true;

        return this.configuration.openRequirements()
                .values()
                .stream()
                .allMatch(requirement -> requirement.test(player, this));
    }

    private void build() {
        for (MenuItem item : this.items) {
            if (!item.shouldShow(player, this))
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

    public Optional<MenuVariable<?>> variable(String name) {
        return this.variables
                .stream()
                .filter(variable -> variable.name().equals(name))
                .findFirst();
    }

    public boolean hasVariable(String name) {
        return this.variables
                .stream()
                .anyMatch(variable -> variable.name().equals(name));
    }

    public void addVariable(MenuVariable<?> variable) {
        this.variables.add(variable);
    }

    public void removeVariable(String name) {
        this.variables.removeIf(variable -> variable.name().equals(name));
    }

    @Unmodifiable
    public List<MenuVariable<?>> variables() {
        return List.copyOf(this.variables);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
