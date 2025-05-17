package com.vulkantechnologies.menu.model.requirement;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.component.MenuComponent;

public interface Requirement extends Predicate<Player>, MenuComponent {


}
