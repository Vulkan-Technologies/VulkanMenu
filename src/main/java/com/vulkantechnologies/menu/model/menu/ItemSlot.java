package com.vulkantechnologies.menu.model.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;

public record ItemSlot(String placeholder, int slot, List<Integer> slots) {

    public List<Integer> slots(Player player, Menu menu) {
        List<Integer> slots = new ArrayList<>();
        if (this.slots != null)
            slots.addAll(this.slots);
        if (slot >= 0)
            slots.add(slot);
        if (placeholder != null && !placeholder.isEmpty()) {
            String processedPlaceholder = VulkanMenu.get().processPlaceholders(player, menu, placeholder);
            if (!processedPlaceholder.contains(",")) {
                try {
                    slots.add(Integer.parseInt(processedPlaceholder));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid placeholder for slot: " + processedPlaceholder);
                }
                return slots;
            }
            String[] split = processedPlaceholder.split(",");
            for (String s : split) {
                try {
                    slots.add(Integer.parseInt(s));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid placeholder for slot: " + s);
                }
            }
        }
        return slots;
    }

    public static ItemSlot empty() {
        return new ItemSlot(null, 0, null);
    }

    public static ItemSlot of(String placeholder) {
        return new ItemSlot(placeholder, 0, null);
    }

    public static ItemSlot of(int slot) {
        return new ItemSlot(null, slot, null);
    }

    public static ItemSlot of(List<Integer> slots) {
        return new ItemSlot(null, 0, slots);
    }
}
