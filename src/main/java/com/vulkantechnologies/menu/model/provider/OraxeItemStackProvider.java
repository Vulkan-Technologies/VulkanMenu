package com.vulkantechnologies.menu.model.provider;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;

public class OraxeItemStackProvider implements ItemStackProvider {

    @Override
    public ItemStack provide(String value) throws SerializationException {
        ItemBuilder item = OraxenItems.getItemById(value);
        if (item == null)
            throw new SerializationException("Item not found: " + value);
        return item.build();
    }

    @Override
    public String prefix() {
        return "oraxen";
    }
}
