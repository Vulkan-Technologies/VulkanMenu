package com.vulkantechnologies.menu.model.action.vulkan;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@ComponentName("refresh-title")
@NoArgsConstructor
public class RefreshTitleAction implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        menu.refreshTitle(player);
    }
}
