package com.vulkantechnologies.menu.model.provider;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;

public class NexoItemStackProvider implements ItemStackProvider {

    @Override
    public ItemStack provide(String value) throws SerializationException {
        ItemBuilder itemBuilder = NexoItems.itemFromId(value);
        if (itemBuilder == null)
            throw new SerializationException("Item not found: " + value);
        return itemBuilder.build();
    }

    @Override
    public String prefix() {
        return "nexo";
    }
}
