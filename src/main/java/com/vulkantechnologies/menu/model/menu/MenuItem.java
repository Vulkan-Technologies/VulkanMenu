package com.vulkantechnologies.menu.model.menu;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.requirement.WrappedRequirement;

@ConfigSerializable
public record MenuItem(int slot, ItemStack item, List<Action> actions,
                       @Nullable List<WrappedRequirement> viewRequirements,
                       List<WrappedRequirement> clickRequirements) {

    public boolean canClick(Player player) {
        if (this.clickRequirements == null || this.clickRequirements.isEmpty())
            return true;

        return this.clickRequirements
                .stream()
                .anyMatch(requirement -> requirement.test(player));
    }

    public boolean shouldShow(Player player) {
        if (this.viewRequirements == null || this.viewRequirements.isEmpty())
            return true;

        return this.viewRequirements
                .stream()
                .anyMatch(requirement -> requirement.test(player));
    }
}
