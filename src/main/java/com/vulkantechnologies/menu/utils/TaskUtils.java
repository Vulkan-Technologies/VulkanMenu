package com.vulkantechnologies.menu.utils;

import org.bukkit.Bukkit;

import com.vulkantechnologies.menu.VulkanMenu;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskUtils {

    public static void runSync(Runnable runnable) {
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(VulkanMenu.get(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        if (!Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTaskAsynchronously(VulkanMenu.get(), runnable);
    }
}
