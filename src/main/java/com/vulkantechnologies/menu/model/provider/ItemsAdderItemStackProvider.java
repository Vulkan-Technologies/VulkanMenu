package com.vulkantechnologies.menu.model.provider;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

import dev.lone.itemsadder.api.CustomStack;

public class ItemsAdderItemStackProvider implements ItemStackProvider {

    @Override
    public ItemStack provide(String value) throws SerializationException {
        CustomStack stack = CustomStack.getInstance(value);
        if (stack == null)
            throw new SerializationException("Invalid ItemsAdder stack: " + value);
        return stack.getItemStack();
    }

    @Override
    public String prefix() {
        return "ia";
    }
}
