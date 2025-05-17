package com.vulkantechnologies.menu.model.action;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.component.MenuComponent;
import com.vulkantechnologies.menu.model.menu.Menu;

public interface Action extends MenuComponent {

    void accept(Player player, Menu menu);

}
