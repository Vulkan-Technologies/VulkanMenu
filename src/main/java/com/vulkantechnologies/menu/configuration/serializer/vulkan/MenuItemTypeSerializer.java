package com.vulkantechnologies.menu.configuration.serializer.vulkan;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.ItemSlot;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;

public class MenuItemTypeSerializer implements TypeSerializer<MenuItem> {

    public static final MenuItemTypeSerializer INSTANCE = new MenuItemTypeSerializer();

    @Override
    public MenuItem deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        ItemSlot slot = node.node("slot").get(ItemSlot.class);

        int priority = node.node("priority").getInt(0);

        ItemWrapper wrapper = node.get(ItemWrapper.class);
        List<Action> actions = node.node("actions").getList(Action.class);
        List<Action> rightClickActions = node.node("right-click-actions").getList(Action.class);
        List<Action> leftClickActions = node.node("left-click-actions").getList(Action.class);
        List<Action> middleClickActions = node.node("middle-click-actions").getList(Action.class);
        List<Action> leftShiftClickActions = node.node("left-shift-click-actions").getList(Action.class);
        List<Action> rightShiftClickActions = node.node("right-shift-click-actions").getList(Action.class);

        List<Requirement> viewRequirements = node.node("view-requirements").getList(Requirement.class);


        Map<String, RequirementWrapper> clickRequirements = this.deserializeRequirements(node, "click-requirements");
        return new MenuItem(
                node.key().toString(),
                slot,
                priority,
                wrapper,
                actions,
                rightClickActions,
                leftClickActions,
                middleClickActions,
                leftShiftClickActions,
                rightShiftClickActions,
                viewRequirements,
                clickRequirements
        );
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable MenuItem obj, @NotNull ConfigurationNode node) throws SerializationException {

    }

    private Map<String, RequirementWrapper> deserializeRequirements(ConfigurationNode node, String id) throws SerializationException {
        Map<String, RequirementWrapper> requirements = new HashMap<>();
        ConfigurationNode parent = node.node(id);
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : parent.childrenMap().entrySet()) {
            String name = entry.getKey().toString();
            ConfigurationNode reqNode = entry.getValue();
            RequirementWrapper wrapper = reqNode.get(RequirementWrapper.class);
            if (wrapper != null) {
                requirements.put(name, wrapper);
            }
        }
        return requirements;
    }
}
