package com.vulkantechnologies.menu.registry;

import java.util.List;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.action.minecraft.CloseInventoryAction;
import com.vulkantechnologies.menu.model.action.minecraft.ConsoleCommandAction;
import com.vulkantechnologies.menu.model.action.minecraft.MessageAction;
import com.vulkantechnologies.menu.model.action.minecraft.PlayerCommandAction;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.ExperienceRequirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.PermissionRequirement;

public class Registries {

    // Component
    public static final ComponentRegistry<Action> ACTION = new ComponentRegistry<>();
    public static final ComponentRegistry<Requirement> REQUIREMENT = new ComponentRegistry<>();

    // Adapters
    public static final ComponentAdapterRegistry<Action> ACTION_ADAPTER = new ComponentAdapterRegistry<>();
    public static final ComponentAdapterRegistry<Requirement> REQUIREMENT_ADAPTER = new ComponentAdapterRegistry<>();


    static {
        // -- Component
        // Action
        List.of(
                ConsoleCommandAction.class,
                PlayerCommandAction.class,
                MessageAction.class,
                CloseInventoryAction.class
        ).forEach(action -> ACTION.register(action));

        // Requirement
        REQUIREMENT.register(PermissionRequirement.class);
        REQUIREMENT.register(ExperienceRequirement.class);

        // -- Adapters
        // Action

        // Requirement
    }
}
