package com.vulkantechnologies.menu.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemMarker {

    private static final NamespacedKey MARKER_KEY = new NamespacedKey("vulkanmenu", "marker");

    public static boolean isMarked(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return false;

        return itemStack.getItemMeta().getPersistentDataContainer().has(MARKER_KEY, PersistentDataType.BYTE);
    }

    public static void mark(ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(MARKER_KEY, PersistentDataType.BYTE, (byte) 1));
    }

    public static void unmark(ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(MARKER_KEY));
    }
}
