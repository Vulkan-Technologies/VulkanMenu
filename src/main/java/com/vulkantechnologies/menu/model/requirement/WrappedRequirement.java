package com.vulkantechnologies.menu.model.requirement;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;

@ConfigSerializable
public record WrappedRequirement(Requirement requirement,
                                 @Nullable List<Action> denyActions) implements Predicate<Player> {

    @Override
    public boolean test(Player player) {
        if (this.requirement == null)
            return true;

        boolean result = this.requirement.test(player);
        if (result)
            return true;

        // Deny actions
        if (this.denyActions != null && !this.denyActions.isEmpty()) {
            for (Action action : this.denyActions) {
                action.accept(player);
            }
        }

        return false;
    }
}
