package com.vulkantechnologies.menu.model;

import org.bukkit.entity.Player;

public interface PlaceholderProcessor {

    String process(Player player, String content);

}
