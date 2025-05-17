package com.vulkantechnologies.menu.model.requirement;

import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

@ConfigSerializable
public record WrappedRequirement(Requirement requirement,
                                 @Nullable List<Action> denyActions) {

    public boolean test(Player player, Menu menu) {
        if (this.requirement == null)
            return true;

        boolean result = this.requirement.test(player, menu);
        if (result)
            return true;

        // Deny actions
        if (this.denyActions != null && !this.denyActions.isEmpty()) {
            for (Action action : this.denyActions) {
                action.accept(player, menu);
            }
        }

        return false;
    }
}
