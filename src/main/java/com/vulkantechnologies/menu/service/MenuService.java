package com.vulkantechnologies.menu.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

public class MenuService {

    private final List<Menu> menus = new CopyOnWriteArrayList<>();

    public void openMenu(Player player, MenuConfiguration configuration) {
        Menu menu = new Menu(player, configuration);
        this.menus.add(menu);

        player.openInventory(menu.getInventory());

        Collection<Action> openActions = configuration.openActions().values();
        if (openActions != null)
            openActions.forEach(action -> action.accept(player));
    }

    public void closeMenu(Player player, Menu menu) {
        Collection<Action> closeActions = menu.configuration().closeActions().values();

        if (closeActions != null)
            closeActions.forEach(action -> action.accept(player));

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
