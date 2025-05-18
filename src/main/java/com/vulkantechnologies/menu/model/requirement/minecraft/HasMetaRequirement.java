package com.vulkantechnologies.menu.model.requirement.minecraft;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.annotation.Single;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("has-meta")
public record HasMetaRequirement(@Single String rawKey, @Single String type) implements Requirement {

    @Override
    public boolean test(Player player, Menu menu) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey("vulkanmenu-meta", rawKey);

        return switch (type) {
            case "STRING" -> container.has(key, PersistentDataType.STRING);
            case "INTEGER" -> container.has(key, PersistentDataType.INTEGER);
            case "DOUBLE" -> container.has(key, PersistentDataType.DOUBLE);
            case "FLOAT" -> container.has(key, PersistentDataType.FLOAT);
            case "LONG" -> container.has(key, PersistentDataType.LONG);
            case "BOOLEAN" -> container.has(key, PersistentDataType.BOOLEAN);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }

}
