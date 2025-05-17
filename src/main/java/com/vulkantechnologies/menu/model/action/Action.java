package com.vulkantechnologies.menu.model.action;

import java.util.function.Consumer;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.Component;

public interface Action extends Consumer<Player>, Component {
}
