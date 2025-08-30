package com.vulkantechnologies.menu.model.action.vulkan;

import java.util.List;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.utils.VariableUtils;

@ComponentName("refresh-slot")
public record RefreshSlotAction(String slot) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        // slot index
        if (VariableUtils.isNumeric(slot)) {
            try {
                int slotIndex = Integer.parseInt(slot);
                if (slotIndex < 0 || slotIndex >= menu.cachedItems().length)
                    throw new IllegalArgumentException("Slot index out of bounds: " + slotIndex);

                menu.refresh(slotIndex);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid slot index: " + slot, e);
            }
            return;
        }

        // item name
        MenuItem item = menu.items()
                .stream()
                .filter(i -> i.id().equals(slot))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item with id '" + slot + "' not found"));

        List<Integer> slots = item.slot().slots(player, menu);
        for (Integer i : slots) {
            if (i < 0 || i >= menu.cachedItems().length)
                throw new IllegalArgumentException("Slot index out of bounds: " + i);

            menu.refresh(i);
        }

    }

}
