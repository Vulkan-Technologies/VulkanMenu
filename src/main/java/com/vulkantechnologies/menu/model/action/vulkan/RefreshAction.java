package com.vulkantechnologies.menu.model.action.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.NoArgsConstructor;

@ComponentName("refresh")
@NoArgsConstructor
public class RefreshAction implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        menu.refresh();
        menu.refreshTitle(player);
    }
}
