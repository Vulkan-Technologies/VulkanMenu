package com.vulkantechnologies.menu.model.action.vulkan.variable;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("remove-variable")
public record RemoveVariableAction(String name) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        menu.removeVariable(name);
    }

}
