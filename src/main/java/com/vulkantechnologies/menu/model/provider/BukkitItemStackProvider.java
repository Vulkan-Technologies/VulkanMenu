package com.vulkantechnologies.menu.model.provider;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.SerializationException;

public class BukkitItemStackProvider implements ItemStackProvider {

    @Override
    public ItemStack provide(String value) throws SerializationException {
        Material material = Material.matchMaterial(value);
        if (material == null)
            throw new SerializationException("Invalid material: " + value);
        return new ItemStack(material);
    }

    @Override
    public String prefix() {
        return "";
    }
}
