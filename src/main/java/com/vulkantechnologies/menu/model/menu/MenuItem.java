package com.vulkantechnologies.menu.model.menu;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ConfigSerializable
public record MenuItem(int slot, ItemStack item, List<Action> actions, List<Requirement> viewRequirements) {

    public boolean shouldShow(Player player) {
        if (this.viewRequirements == null || this.viewRequirements.isEmpty())
            return true;

        return this.viewRequirements
                .stream()
                .anyMatch(requirement -> requirement.test(player));
    }
}
