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
public record MenuItem(String id, ItemSlot slot, int priority, ItemWrapper item, @Nullable List<Action> actions,
                       @Nullable List<Action> rightClickActions,
                       @Nullable List<Action> leftClickActions,
                       @Nullable List<Action> middleClickActions,
                       @Nullable List<Action> leftShiftClickActions,
                       @Nullable List<Action> rightShiftClickActions,
                       @Nullable List<Requirement> viewRequirements,
                       Map<String, RequirementWrapper> clickRequirements) {

    public boolean hasSlot(Player player, Menu menu, int slot) {
        return this.slot.slots(player, menu).contains(slot);
    }

    public void handleClick(Player player, Menu menu, ClickType clickType) {
        this.handleClick(player, menu, this.actions);
        switch (clickType) {
            case LEFT -> handleClick(player, menu, this.leftClickActions);
            case RIGHT -> handleClick(player, menu, this.rightClickActions);
            case MIDDLE -> handleClick(player, menu, this.middleClickActions);
            case SHIFT_LEFT -> handleClick(player, menu, this.leftShiftClickActions);
            case SHIFT_RIGHT -> handleClick(player, menu, this.rightShiftClickActions);
        }
    }

    private void handleClick(Player player, Menu menu, List<Action> actions) {
        // Requirements
        if (actions == null
            || actions.isEmpty()) {
            return;
        }

        if (!this.canClick(player, menu)) {
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
                .allMatch(requirement -> requirement.test(player, menu));
    }

    public boolean shouldShow(Player player, Menu menu) {
        if (this.viewRequirements == null || this.viewRequirements.isEmpty())
            return true;

        return this.viewRequirements
                .stream()
                .allMatch(requirement -> requirement.test(player, menu));
    }
}
