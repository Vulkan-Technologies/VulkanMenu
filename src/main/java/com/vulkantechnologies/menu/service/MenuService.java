package com.vulkantechnologies.menu.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.Menu;

public class MenuService {

    private final List<Menu> menus = new CopyOnWriteArrayList<>();

    public void openMenu(Player player, MenuConfiguration configuration) {
        Menu menu = new Menu(configuration);
        this.menus.add(menu);
        player.openInventory(menu.getInventory());
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
