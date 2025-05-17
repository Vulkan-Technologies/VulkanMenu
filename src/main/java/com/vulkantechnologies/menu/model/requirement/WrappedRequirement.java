package com.vulkantechnologies.menu.model.requirement;

import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import com.vulkantechnologies.menu.model.action.Action;

@ConfigSerializable
public record WrappedRequirement(Requirement requirement, @Nullable List<Action> denyActions) implements Requirement {

    @Override
    public boolean test(Player player) {
        boolean result = this.requirement.test(player);
        if (result)
            return true;

        if (this.denyActions != null && !this.denyActions.isEmpty()) {
            for (Action action : this.denyActions) {
                action.accept(player);
            }
        }
        return false;
    }
}
