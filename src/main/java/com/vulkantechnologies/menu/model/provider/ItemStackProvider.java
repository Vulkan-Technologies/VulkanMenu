package com.vulkantechnologies.menu.model.provider;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

public interface ItemStackProvider {

    ItemStack provide(String value) throws SerializationException;

    String prefix();
}
