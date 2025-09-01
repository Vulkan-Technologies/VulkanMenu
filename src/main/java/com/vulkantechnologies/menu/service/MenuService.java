package com.vulkantechnologies.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vulkantechnologies.menu.model.variable.MenuVariable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.event.VMenuCloseEvent;
import com.vulkantechnologies.menu.event.VMenuOpenEvent;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.utils.TaskUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuService {

    private final VulkanMenu plugin;
    private final List<Menu> menus = new CopyOnWriteArrayList<>();

    public void openMenu(Player player, String menuId, MenuVariable<?>... variables) {
        this.plugin.configuration()
                .findByName(menuId)
                .ifPresentOrElse(menu -> this.openMenu(player, menu.menu(), variables),
                        () -> this.plugin.getSLF4JLogger().warn("Menu with ID {} not found", menuId));
    }

    public void openMenu(Player player, MenuConfiguration configuration, MenuVariable<?>... variables) {
        TaskUtils.runSync(() -> {
            Menu menu = new Menu(player, configuration);

            for (MenuVariable<?> variable : variables) {
                menu.addVariable(variable);
            }

            // Event & requirements check
            VMenuOpenEvent event = new VMenuOpenEvent(player, menu);
            if (!event.callEvent() || !menu.canOpen(player))
                return;

            // Register
            this.menus.add(menu);

            // Open
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                player.openInventory(menu.getInventory());
                // Actions
                List<Action> openActions = configuration.openActions();
                if (openActions != null)
                    openActions.forEach(action -> action.accept(player, menu));
            }, 1L);
        });
    }

    public void closeMenu(Player player, Menu menu) {
        TaskUtils.runSync(() -> {
            // Action
            List<Action> closeActions = menu.configuration().closeActions();
            if (closeActions != null)
                closeActions.forEach(action -> action.accept(player, menu));

            // Event
            new VMenuCloseEvent(player, menu).callEvent();

            // Update inventory
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);

            // Remove menu
            this.menus.remove(menu);
        });
    }

    public Optional<Menu> findByPlayer(Player player) {
        return this.menus.stream()
                .filter(menu -> menu.player().getUniqueId().equals(player.getUniqueId()))
                .findFirst();
    }

    public List<Player> closeAll(MenuConfiguration configuration) {
        List<Player> players = new ArrayList<>();
        this.menus.stream()
                .filter(menu -> menu.configuration().equals(configuration))
                .forEach(menu -> {
                    Player player = menu.player();
                    players.add(player);

                    TaskUtils.runSync(player::closeInventory);

                    this.closeMenu(player, menu);
                });
        return players;
    }

    public List<Menu> findById(MenuConfiguration configuration) {
        return this.menus.stream()
                .filter(menu -> menu.configuration().equals(configuration))
                .toList();
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
    }

    @Unmodifiable
    public List<Menu> menus() {
        return List.copyOf(this.menus);
    }
}
