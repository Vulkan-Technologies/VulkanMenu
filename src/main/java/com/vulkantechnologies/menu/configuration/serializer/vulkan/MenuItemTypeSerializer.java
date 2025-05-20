package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

public class MenuItemTypeSerializer implements TypeSerializer<MenuItem> {

    public static final MenuItemTypeSerializer INSTANCE = new MenuItemTypeSerializer();

    @Override
    public MenuItem deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        ConfigurationNode slotNode = node.node("slot");
        ConfigurationNode slotsNode = node.node("slots");

        List<Integer> slots = new ArrayList<>();
        if (slotNode.virtual()) {
            if (slotsNode.virtual()) {
                throw new SerializationException("No slot or slots defined");
            } else {
                for (ConfigurationNode slot : slotsNode.childrenList()) {
                    slots.add(slot.getInt());
                }
            }
        } else {
            slots.add(slotNode.getInt());
        }

        int priority = node.node("priority").getInt(0);

        ItemWrapper wrapper = node.get(ItemWrapper.class);
        List<Action> actions = node.node("actions").getList(Action.class);
        List<Requirement> viewRequirements = node.node("view-requirements").getList(Requirement.class);
        Map<String, RequirementWrapper> clickRequirements = new HashMap<>();
        for (ConfigurationNode clickRequirement : node.node("click-requirements").childrenList()) {
            String name = clickRequirement.key().toString();
            RequirementWrapper requirement = clickRequirement.get(RequirementWrapper.class);
            if (requirement != null) {
                clickRequirements.put(name, requirement);
            }
        }
        return new MenuItem(slots, priority, wrapper, actions, viewRequirements, clickRequirements);
    }

    @Override
    public void serialize(Type type, @Nullable MenuItem obj, ConfigurationNode node) throws SerializationException {

    }
}
