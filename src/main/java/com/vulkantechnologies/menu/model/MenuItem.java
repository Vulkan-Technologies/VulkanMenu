package com.vulkantechnologies.menu.model;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record MenuItem(int slot, ItemStack item) {
}
