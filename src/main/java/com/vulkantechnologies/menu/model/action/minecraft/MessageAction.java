package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;

@ComponentName("message")
public record MessageAction(ComponentWrapper message) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        player.sendMessage(message.build(player, menu));
    }

}
