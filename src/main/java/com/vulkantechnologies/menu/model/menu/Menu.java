package com.vulkantechnologies.menu.model.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.variable.MenuVariable;
import com.vulkantechnologies.menu.utils.VariableUtils;

import lombok.Data;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Data
public class Menu implements InventoryHolder {

    private final UUID uniqueId;
    private final Player player;
    private final MenuConfiguration configuration;
    private final Inventory inventory;
    private final List<MenuItem> items;
    private final List<MenuVariable<?>> variables;
    private final ItemStack[] cachedItems;

    public Menu(Player player, MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.player = player;
        this.configuration = configuration;
        this.variables = new CopyOnWriteArrayList<>();
        this.inventory = Bukkit.createInventory(this, configuration.size(), configuration.title().build(player, this));
        this.items = new ArrayList<>(configuration.items().values());
        this.cachedItems = new ItemStack[configuration.size() + 36];

        this.configuration.variables().forEach((key, value) -> {
            CompactAdapter<?> adapter = VariableUtils.findAdapter(value);

            this.variables.add(new MenuVariable(key, adapter.type(), adapter, adapter.adapt(new CompactContext(value))));
        });

        this.build(true);
    }

    public boolean canOpen(Player player) {
        if (this.configuration.openRequirements() == null || this.configuration.openRequirements().isEmpty())
            return true;

        return this.configuration.openRequirements()
                .values()
                .stream()
                .allMatch(requirement -> requirement.test(player, this));
    }

    public void refresh(int slot) {
        this.inventory.clear(slot);
        this.getItem(slot).ifPresent(item -> {
            ItemStack itemStack = item.item().build(player, this);
            if (slot < this.configuration.size())
                this.inventory.setItem(slot, itemStack);
            this.cachedItems[slot] = itemStack;
        });
    }

    public void refresh() {
        this.inventory.clear();

        List<ItemStack> items = this.build(false);
        for (int i = 0; i < items.size(); i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }

    public List<ItemStack> build(boolean bottom) {
        List<ItemStack> items = new ArrayList<>();

        int size = this.configuration.size();

        // Top inventory
        for (int i = 0; i < size; i++) {
            int finalI = i;
            this.getItem(i)
                    .filter(item -> item.shouldShow(player, this))
                    .ifPresentOrElse(item -> {
                                ItemStack itemStack = item.item().build(player, this);
                                items.add(itemStack);
                                this.inventory.setItem(finalI, itemStack);
                            },
                            () -> items.add(new ItemStack(Material.AIR)));

            // Cache the item
            this.cachedItems[i] = items.get(i);
        }

        // Bottom inventory
        if (!bottom)
            return items;

        for (int i = 0; i < 36; i++) {
            this.getItem(i + size)
                    .filter(item -> item.shouldShow(player, this))
                    .ifPresentOrElse(item -> items.add(item.item().build(player, this)),
                            () -> items.add(new ItemStack(Material.AIR)));

            // Cache the item
            this.cachedItems[i] = items.get(i);
        }

        return items;
    }

    public Optional<MenuItem> getItem(int slot) {
        return this.items
                .stream()
                .filter(item -> item.hasSlot(slot))
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

    public TagResolver variableResolver() {
        return TagResolver.resolver(this.variables.toArray(TagResolver[]::new));
    }

    public String injectVariable(String text) {
        for (MenuVariable<?> variable : this.variables) {
            text = text.replace("<variable-" + variable.name() + ">", variable.value().toString());
        }
        return text;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
