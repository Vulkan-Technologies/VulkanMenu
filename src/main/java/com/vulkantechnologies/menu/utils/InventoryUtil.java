package com.vulkantechnologies.menu.utils;

import com.vulkantechnologies.menu.model.menu.Menu;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InventoryUtil {

    public static boolean isOnMenu(Player player, Menu menu) {
        try {
            Object view = player.getOpenInventory();
            Method getTopInventory = view.getClass().getMethod("getTopInventory");
            getTopInventory.setAccessible(true);
            Object inventory = getTopInventory.invoke(view);
            Method getHolder = inventory.getClass().getMethod("getHolder");
            getHolder.setAccessible(true);
            Object holder = getHolder.invoke(inventory);
            return holder != null && holder.equals(menu);
        } catch (Exception e) {
            return false;
        }
    }

}