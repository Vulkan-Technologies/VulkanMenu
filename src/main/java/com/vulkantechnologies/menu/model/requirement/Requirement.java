package com.vulkantechnologies.menu.model.requirement;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.model.Component;

public interface Requirement extends Predicate<Player>, Component {


}
