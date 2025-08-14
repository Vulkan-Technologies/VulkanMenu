package com.vulkantechnologies.menu.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InventoryUtil {

    public static void setTitle(Player player, String title) {
        try {
            Object view = player.getOpenInventory();
            Method setTitle = view.getClass().getMethod("setTitle", String.class);
            setTitle.setAccessible(true);
            setTitle.invoke(view, title);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
