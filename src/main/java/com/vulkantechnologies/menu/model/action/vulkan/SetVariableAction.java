package com.vulkantechnologies.menu.model.action.vulkan;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.variable.MenuVariable;
import com.vulkantechnologies.menu.utils.VariableUtils;

@ComponentName("set-variable")
public record SetVariableAction(String name, String value) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        Class<?> type = VariableUtils.getType(value);

        MenuVariable variable = menu.variable(name)
                .orElseGet(() -> {
                    MenuVariable newVariable = new MenuVariable(name, type);
                    menu.addVariable(newVariable);
                    return newVariable;
                });

        Object parsedValue = VariableUtils.parseValue(value, type);
        if (parsedValue == null)
            throw new IllegalArgumentException("Invalid value for variable: " + name);

        if (!type.isInstance(parsedValue))
            throw new IllegalArgumentException("Value type does not match variable type: " + name);

        variable.value(parsedValue);
    }

}
