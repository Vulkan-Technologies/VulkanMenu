package com.vulkantechnologies.menu.model.action.minecraft.meta;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.utils.VariableUtils;

@ComponentName("set-meta")
public record SetMetaAction(@Single String rawKey, String value) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey("vulkanmenu-meta", rawKey);

        if (VariableUtils.isDouble(value))
            container.set(key, PersistentDataType.DOUBLE, Double.parseDouble(value));
        else if (VariableUtils.isFloat(value))
            container.set(key, PersistentDataType.FLOAT, Float.parseFloat(value));
        else if (VariableUtils.isInteger(value))
            container.set(key, PersistentDataType.INTEGER, Integer.parseInt(value));
        else if (VariableUtils.isLong(value))
            container.set(key, PersistentDataType.LONG, Long.parseLong(value));
        else if (VariableUtils.isBoolean(value))
            container.set(key, PersistentDataType.BOOLEAN, Boolean.parseBoolean(value));
        else
            container.set(key, PersistentDataType.STRING, value);
    }
}
