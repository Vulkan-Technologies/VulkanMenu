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
        switch (clickType) {
            case LEFT -> handleClick(player, menu, this.leftClickActions, true);
            case RIGHT -> handleClick(player, menu, this.rightClickActions, true);
            case MIDDLE -> handleClick(player, menu, this.middleClickActions, true);
            case SHIFT_LEFT -> handleClick(player, menu, this.leftShiftClickActions, true);
            case SHIFT_RIGHT -> handleClick(player, menu, this.rightShiftClickActions, true);
            default -> handleClick(player, menu, this.actions, false);
        }
    }

    private void handleClick(Player player, Menu menu, List<Action> actions, boolean useDefault) {
        if (actions == null
            || actions.isEmpty()) {
            if (useDefault)
                handleClick(player, menu, this.actions, false);
            return;
        }

        // Requirements
        if (!this.canClick(player, menu)) {
            System.out.println("CLICK REQUIREMENTS NOT MET for " + this.id);
            return;
        }

        System.out.println("CLICK REQUIREMENTS MET for " + this.id);
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
