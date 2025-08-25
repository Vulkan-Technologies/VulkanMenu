package com.vulkantechnologies.menu.utils;

import com.vulkantechnologies.menu.model.menu.Menu;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class InventoryUtil {

    private static Method getTopInventoryMethod;
    private static Method getHolderMethod;

    public static boolean isOnMenu(Player player, Menu menu) {
        if (player == null || menu == null) return false;

        try {
            Object view = player.getOpenInventory();

            if (getTopInventoryMethod == null) {
                getTopInventoryMethod = view.getClass().getMethod("getTopInventory");
                getTopInventoryMethod.setAccessible(true);
            }

            Object inventory = getTopInventoryMethod.invoke(view);
            if (inventory == null) return false;

            if (getHolderMethod == null) {
                getHolderMethod = inventory.getClass().getMethod("getHolder");
                getHolderMethod.setAccessible(true);
            }

            Object holder = getHolderMethod.invoke(inventory);
            return holder != null && holder.equals(menu);

        } catch (Exception e) {
            return false;
        }
    }
}