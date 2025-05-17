package com.vulkantechnologies.menu.model.menu;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

@ConfigSerializable
public record MenuItem(int slot, ItemWrapper item, List<Action> actions,
                       @Nullable List<Requirement> viewRequirements,
                       Map<String, RequirementWrapper> clickRequirements) {

    public boolean canClick(Player player, Menu menu) {
        if (this.clickRequirements == null || this.clickRequirements.isEmpty())
            return true;

        return this.clickRequirements.values()
                .stream()
                .anyMatch(requirement -> requirement.test(player, menu));
    }

    public boolean shouldShow(Player player, Menu menu) {
        if (this.viewRequirements == null || this.viewRequirements.isEmpty())
            return true;

        return this.viewRequirements
                .stream()
                .anyMatch(requirement -> requirement.test(player, menu));
    }
}
