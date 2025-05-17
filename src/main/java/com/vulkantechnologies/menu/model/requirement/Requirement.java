package com.vulkantechnologies.menu.model.requirement;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.component.MenuComponent;
import com.vulkantechnologies.menu.model.menu.Menu;

public interface Requirement extends MenuComponent {

    boolean test(Player player, Menu menu);

}
