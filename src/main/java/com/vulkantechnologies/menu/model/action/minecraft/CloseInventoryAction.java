package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;

import lombok.NoArgsConstructor;

@ComponentName("close")
@NoArgsConstructor
public class CloseInventoryAction implements Action {

    @Override
    public void accept(Player player) {
        player.closeInventory();
    }

}
