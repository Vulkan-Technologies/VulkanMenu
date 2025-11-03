package com.vulkantechnologies.menu.model.layout;

import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.supplier.ItemStackSupplier;

@ConfigSerializable
public record Layout(String namespace, ItemStackSupplier supplier, List<Integer> slots, @Nullable String pageVariable) {

    public int maxPage(Menu menu, Player player) {
        int totalItems = supplier.get(menu, player).size();
        return (int) Math.ceil((double) totalItems / slots.size());
    }
}
