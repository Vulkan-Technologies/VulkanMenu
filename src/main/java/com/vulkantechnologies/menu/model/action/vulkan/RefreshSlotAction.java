package com.vulkantechnologies.menu.model.action.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("refresh-slot")
public record RefreshSlotAction(int slot) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        menu.refresh(slot);
    }

}
