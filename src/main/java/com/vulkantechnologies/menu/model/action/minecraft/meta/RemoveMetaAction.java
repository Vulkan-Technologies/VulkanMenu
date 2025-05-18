package com.vulkantechnologies.menu.model.action.minecraft.meta;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("remove-meta")
public record RemoveMetaAction(String rawKey) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey("vulkanmenu-meta", rawKey);

        container.remove(key);
    }
}
