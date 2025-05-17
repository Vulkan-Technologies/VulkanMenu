package com.vulkantechnologies.menu.model.action.vulkan.variable;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.variable.MenuVariable;

@ComponentName("set-variable")
public record SetVariableAction(@Single String name, @Single String value) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        MenuVariable<?> variable = menu.variable(name)
                .orElseThrow(() -> new IllegalArgumentException("Variable not found: " + name));

        variable.value(value);
    }

}
