package com.vulkantechnologies.menu.model.supplier.impl;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.supplier.ItemStackSupplier;

@ConfigSerializable
public record DirectItemStackSupplier(String namespace, List<ItemStack> items) implements ItemStackSupplier {

    @Override
    public List<ItemStack> get(Menu menu, Player player) {
        return this.items;
    }

}
