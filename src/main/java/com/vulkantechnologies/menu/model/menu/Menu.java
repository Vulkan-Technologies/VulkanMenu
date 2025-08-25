package com.vulkantechnologies.menu.model.menu;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vulkantechnologies.menu.VMenuAPI;
import com.vulkantechnologies.menu.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.adapter.CompactContext;
import com.vulkantechnologies.menu.model.variable.MenuVariable;
import com.vulkantechnologies.menu.utils.VariableUtils;

import lombok.Data;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Data
public class Menu implements InventoryHolder {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    private final UUID uniqueId;
    private final Player player;
    private final MenuConfiguration configuration;
    private final List<MenuItem> items;
    private final List<MenuVariable<?>> variables;
    private final ItemStack[] cachedItems;

    private Inventory inventory;

    // Refresh
    private boolean refreshing;
    private long creationTime;
    private long lastRefreshTime;

    public Menu(Player player, MenuConfiguration configuration) {
        this.uniqueId = UUID.randomUUID();
        this.player = player;
        this.configuration = configuration;
        this.variables = new CopyOnWriteArrayList<>();
        this.configuration.variables().forEach((key, value) -> {
            CompactAdapter<?> adapter = VariableUtils.findAdapter(value);

            this.variables.add(new MenuVariable(key, adapter.type(), adapter, adapter.adapt(new CompactContext(value))));
        });

        this.inventory = Bukkit.createInventory(this, configuration.size(), configuration.title().build(player, this));
        this.items = new ArrayList<>(configuration.items().values());
        this.cachedItems = new ItemStack[configuration.size() + 36];

        this.creationTime = System.currentTimeMillis();
        this.lastRefreshTime = System.currentTimeMillis();
        this.refreshing = false;
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

    public void refresh(int slot) {
        this.inventory.clear(slot);
        this.getShownItem(slot).ifPresent(item -> {
            ItemStack itemStack = item.item().build(player, this);
            this.setItem(slot, itemStack);
            this.cachedItems[slot] = itemStack;
        });
    }

    public void refreshTitle(Player player) {
        Component newTitle = this.configuration.title().build(player, this);
        if (!InventoryUtil.isOnMenu(player, this)) {
            return;
        }
        Inventory newInventory = Bukkit.createInventory(this, configuration.size(), newTitle);
        newInventory.setContents(inventory.getContents());
        inventory = newInventory;
        this.refreshing = true;
        player.openInventory(newInventory);
    }

    public void refresh() {
        this.inventory.clear();

        List<ItemStack> items = this.build();
        for (int slot = 0; slot < items.size(); slot++) {
            ItemStack itemStack = items.get(slot);
            this.setItem(slot, itemStack);
        }

        this.lastRefreshTime = System.currentTimeMillis();
    }

    public List<ItemStack> build() {
        int size = this.configuration.size();
        int totalSize = size + 36;

        List<ItemStack> items = new ArrayList<>(totalSize);
        ItemStack air = new ItemStack(Material.AIR);

        Arrays.fill(this.cachedItems, air);

        long startTime = System.currentTimeMillis();

        // Top inventory
        for (int i = 0; i < size; i++) {
            MenuItem shown = this.getShownItem(i).orElse(null);
            ItemStack stack = (shown != null) ? shown.item().build(player, this) : air;

            items.add(stack);
            this.setItem(i, stack);
            this.cachedItems[i] = stack;
        }

        // Bottom inventory
        for (int i = size; i < totalSize; i++) {
            MenuItem shown = this.getShownItem(i).orElse(null);
            ItemStack stack = (shown != null && shown.shouldShow(player, this))
                    ? shown.item().build(player, this)
                    : air;

            items.add(stack);
            this.cachedItems[i] = stack;
        }

        return items;
    }


    public Optional<MenuItem> getShownItem(int slot) {
        List<MenuItem> menuItems = new ArrayList<>();

        // Search by slot
        for (MenuItem item : this.items) {
            if (item.hasSlot(this.player, this, slot))
                menuItems.add(item);
        }

        if (menuItems.isEmpty())
            return Optional.empty();

        // Sort by priority
        menuItems.sort((item1, item2) -> {
            if (item1.priority() == item2.priority())
                return 0;
            return item1.priority() > item2.priority() ? -1 : 1;
        });

        // Filter by requirements
        menuItems.removeIf(item -> !item.shouldShow(player, this));

        // Return the first item
        if (menuItems.isEmpty())
            return Optional.empty();

        // Return the first item
        return Optional.of(menuItems.getFirst());
    }

    public List<MenuItem> items(int slot) {
        return this.items
                .stream()
                .filter(item -> item.hasSlot(this.player, this, slot))
                .toList();
    }

    public Optional<MenuItem> getItem(int slot) {
        return this.items
                .stream()
                .filter(item -> item.hasSlot(this.player, this, slot))
                .findFirst();
    }

    public Optional<MenuVariable<?>> variable(String name) {
        return this.variables
                .stream()
                .filter(variable -> variable.name().equals(name))
                .findFirst();
    }

    private void setItem(int slot, ItemStack item) {
        if (slot < this.configuration.size())
            this.inventory.setItem(slot, item);
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
