package com.vulkantechnologies.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuService {

    private final VulkanMenu plugin;
    private final List<Menu> menus = new CopyOnWriteArrayList<>();

    public void openMenu(Player player, String menuId) {
        this.plugin.configuration()
                .findByName(menuId)
                .ifPresentOrElse(menu -> this.openMenu(player, menu.menu()),
                        () -> this.plugin.getSLF4JLogger().warn("Menu with ID {} not found", menuId));
    }

    public void openMenu(Player player, MenuConfiguration configuration) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Menu menu = new Menu(player, configuration);
            if (!menu.canOpen(player)) {
                return;
            }

            this.menus.add(menu);

            player.openInventory(menu.getInventory());

            Map<String, Action> openActions = configuration.openActions();
            if (openActions != null)
                openActions.values().forEach(action -> action.accept(player, menu));
        });
    }

    public void closeMenu(Player player, Menu menu) {
        Map<String, Action> closeActions = menu.configuration().closeActions();

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (closeActions != null)
                closeActions.values().forEach(action -> action.accept(player, menu));

            player.closeInventory();
        });

        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        this.menus.remove(menu);
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
                    Map<String, Action> closeActions = menu.configuration().closeActions();

                    if (closeActions != null)
                        closeActions.values().forEach(action -> action.accept(player, menu));

                    Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
                    this.menus.remove(menu);
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
