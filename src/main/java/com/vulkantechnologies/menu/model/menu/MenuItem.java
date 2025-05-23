package com.vulkantechnologies.menu.model.menu;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

@ConfigSerializable
public record MenuItem(List<Integer> slots, int priority, ItemWrapper item, List<Action> actions,
                       List<Action> rightClickActions,
                       List<Action> leftClickActions,
                       List<Action> middleClickActions,
                       @Nullable List<Requirement> viewRequirements,
                       Map<String, RequirementWrapper> clickRequirements,
                       Map<String, RequirementWrapper> leftClickRequirements,
                       Map<String, RequirementWrapper> rightClickRequirements,
                       Map<String, RequirementWrapper> middleClickRequirements) {

    public boolean hasSlot(int slot) {
        if (this.slots == null || this.slots.isEmpty())
            return true;

        return this.slots.contains(slot);
    }

    public void handleClick(Player player, Menu menu, ClickType clickType) {
        switch (clickType) {
            case LEFT -> handleClick(player, menu, this.leftClickActions, this.leftClickRequirements, true);
            case RIGHT -> handleClick(player, menu, this.rightClickActions, this.rightClickRequirements, true);
            case MIDDLE -> handleClick(player, menu, this.middleClickActions, this.middleClickRequirements, true);
            default -> handleClick(player, menu, this.actions, this.clickRequirements, false);
        }
    }

    private void handleClick(Player player, Menu menu, List<Action> actions, Map<String, RequirementWrapper> requirements, boolean useDefault) {
        if (actions == null
            || actions.isEmpty()
            || requirements == null
            || requirements.isEmpty()
            || !requirements.values()
                .stream()
                .allMatch(requirement -> requirement.test(player, menu))) {
            if (useDefault)
                handleClick(player, menu, this.actions, this.clickRequirements, false);
            return;
        }

        for (Action action : actions) {
            action.accept(player, menu);
        }
    }

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
