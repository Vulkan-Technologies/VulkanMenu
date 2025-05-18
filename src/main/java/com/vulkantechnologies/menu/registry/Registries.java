package com.vulkantechnologies.menu.registry;

import java.util.List;

import com.vulkantechnologies.menu.configuration.adapter.compact.*;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.action.minecraft.*;
import com.vulkantechnologies.menu.model.action.vulkan.OpenMenuAction;
import com.vulkantechnologies.menu.model.action.vulkan.RefreshAction;
import com.vulkantechnologies.menu.model.action.vulkan.RefreshSlotAction;
import com.vulkantechnologies.menu.model.action.vulkan.variable.RemoveVariableAction;
import com.vulkantechnologies.menu.model.action.vulkan.variable.SetVariableAction;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.ExperienceRequirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.PermissionRequirement;

public class Registries {

    // MenuComponent
    public static final ComponentRegistry<Action> ACTION = new ComponentRegistry<>();
    public static final ComponentRegistry<Requirement> REQUIREMENT = new ComponentRegistry<>();

    // Adapters
    public static final ComponentAdapterRegistry<Action> ACTION_ADAPTER = new ComponentAdapterRegistry<>();
    public static final ComponentAdapterRegistry<Requirement> REQUIREMENT_ADAPTER = new ComponentAdapterRegistry<>();
    public static final CompactAdapterRegistry COMPACT_ADAPTER = new CompactAdapterRegistry();

    static {
        // -- MenuComponent
        // Action
        List.of(
                ConsoleCommandAction.class,
                PlayerCommandAction.class,
                MessageAction.class,
                CloseInventoryAction.class,
                PlayerCommandAction.class,
                OpenMenuAction.class,
                SetVariableAction.class,
                RemoveVariableAction.class,
                RefreshAction.class,
                RefreshSlotAction.class,
                TeleportAction.class,
                ActionBarAction.class,
                SoundAction.class,
                BroadcastSoundAction.class,
                BroadcastAction.class
        ).forEach(ACTION::register);

        // Requirement
        List.of(
                PermissionRequirement.class,
                ExperienceRequirement.class
        ).forEach(REQUIREMENT::register);

        // -- Adapters
        // Action

        // Requirement

        // Compact
        List.of(
                CompactBooleanAdapter.INSTANCE,
                CompactDoubleAdapter.INSTANCE,
                CompactIntegerAdapter.INSTANCE,
                CompactBooleanAdapter.INSTANCE,
                CompactStringAdapter.INSTANCE,
                CompactComponentAdapter.INSTANCE,
                CompactLocationAdapter.INSTANCE,
                CompactSoundAdapter.INSTANCE,
                CompactComponentWrapperAdapter.INSTANCE
        ).forEach(COMPACT_ADAPTER::register);
    }
}
