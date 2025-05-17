package com.vulkantechnologies.menu.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

public class MenuService {

    private final List<Menu> menus = new CopyOnWriteArrayList<>();

    public void openMenu(Player player, MenuConfiguration configuration) {
        if (!configuration.canOpen(player))
            return;

        Menu menu = new Menu(player, configuration);
        this.menus.add(menu);

        player.openInventory(menu.getInventory());

        Map<String, Action> openActions = configuration.openActions();
        if (openActions != null)
            openActions.values().forEach(action -> action.accept(player));
    }

    public void closeMenu(Player player, Menu menu) {
        Map<String, Action> closeActions = menu.configuration().closeActions();

        if (closeActions != null)
            closeActions.values().forEach(action -> action.accept(player));

        this.menus.remove(menu);
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
