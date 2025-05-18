package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.title.Title;

@ComponentName("title")
public record TitleAction(Title title) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        player.showTitle(title);
    }

}
