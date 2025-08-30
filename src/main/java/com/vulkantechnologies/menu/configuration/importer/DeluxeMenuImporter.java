package com.vulkantechnologies.menu.configuration.importer;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.CommandConfiguration;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.configuration.serializer.vulkan.MenuComponentTypeSerializer;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;
import com.vulkantechnologies.menu.model.menu.ItemSlot;
import com.vulkantechnologies.menu.model.menu.MenuItem;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeluxeMenuImporter implements ConfigurationImporter {

    private final VulkanMenu plugin;

    @Override
    public MenuConfiguration process(CommentedConfigurationNode node) {
        MenuConfiguration.MenuConfigurationBuilder builder = MenuConfiguration.builder();

        // Size
        CommentedConfigurationNode sizeNode = node.node("size");
        if (sizeNode.virtual())
            throw new IllegalArgumentException("Size is required");
        builder.size(sizeNode.getInt());

        // Title
        CommentedConfigurationNode titleNode = node.node("menu_title");
        if (titleNode.virtual())
            throw new IllegalArgumentException("Title is required");
        try {
            String title = titleNode.getString();
            builder.title(new ComponentWrapper(title));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse title", e);
        }

        // Open command
        CommentedConfigurationNode openCommandNode = node.node("open_command");
        if (!openCommandNode.virtual()) {
            CommandConfiguration.CommandConfigurationBuilder commandBuilder = CommandConfiguration.builder();

            // Handle single command string or command object
            if (openCommandNode.isMap()) {
                CommentedConfigurationNode nameNode = openCommandNode.node("command");
                if (!nameNode.virtual()) {
                    commandBuilder.name(nameNode.getString());
                }

                CommentedConfigurationNode aliasesNode = openCommandNode.node("aliases");
                if (!aliasesNode.virtual() && aliasesNode.isList()) {
                    try {
                        List<String> aliases = aliasesNode.getList(String.class);
                        if (aliases != null && !aliases.isEmpty()) {
                            commandBuilder.aliases(aliases);
                        }
                    } catch (SerializationException e) {
                        plugin.getLogger().warning("Failed to parse command aliases: " + e.getMessage());
                    }
                }

                CommentedConfigurationNode permissionNode = openCommandNode.node("permission");
                if (!permissionNode.virtual()) {
                    commandBuilder.permission(permissionNode.getString());
                }
            } else {
                String openCommand = openCommandNode.getString();
                if (openCommand != null && !openCommand.isEmpty()) {
                    commandBuilder.name(openCommand);
                }
            }

            builder.openCommand(commandBuilder.build());
        }

        // Open requirements
        CommentedConfigurationNode openRequirementsNode = node.node("open_requirements");
        if (!openRequirementsNode.virtual()) {
            Map<String, RequirementWrapper> openRequirements = parseRequirements(openRequirementsNode);
            if (!openRequirements.isEmpty()) {
                builder.openRequirements(openRequirements);
            }
        }

        // Open actions
        CommentedConfigurationNode openActionsNode = node.node("open_actions");
        if (!openActionsNode.virtual()) {
            List<Action> openActions = parseActions(openActionsNode);
            if (!openActions.isEmpty()) {
                builder.openActions(openActions);
            }
        }

        // Close actions
        CommentedConfigurationNode closeActionsNode = node.node("close_actions");
        if (!closeActionsNode.virtual()) {
            List<Action> closeActions = parseActions(closeActionsNode);
            if (!closeActions.isEmpty()) {
                builder.closeActions(closeActions);
            }
        }

        // Items
        CommentedConfigurationNode itemsNode = node.node("items");
        if (!itemsNode.virtual()) {
            Map<String, MenuItem> items = new HashMap<>();

            itemsNode.childrenMap().forEach((key, itemNode) -> {
                try {
                    MenuItem item = parseMenuItem(key.toString(), itemNode);
                    if (item != null) {
                        items.put(key.toString(), item);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to parse item " + key + ": " + e.getMessage());
                }
            });

            builder.items(items);
        }

        return builder.build();
    }

    private MenuItem parseMenuItem(String id, CommentedConfigurationNode node) {
        // Parse slot(s)
        ItemSlot slot = parseSlot(node);
        if (slot == null) {
            plugin.getLogger().warning("Item " + id + " has no valid slot");
            return null;
        }

        // Parse item wrapper
        ItemWrapper itemWrapper = parseItemWrapper(node);
        if (itemWrapper == null) {
            plugin.getLogger().warning("Item " + id + " has no valid material");
            return null;
        }

        // Parse priority
        int priority = node.node("priority").getInt(0);

        // Parse actions for different click types
        List<Action> actions = parseActions(node.node("actions"));
        List<Action> leftClickActions = parseActions(node.node("left_click_actions"));
        List<Action> rightClickActions = parseActions(node.node("right_click_actions"));
        List<Action> middleClickActions = parseActions(node.node("middle_click_actions"));
        List<Action> leftShiftClickActions = parseActions(node.node("shift_left_click_actions"));
        List<Action> rightShiftClickActions = parseActions(node.node("shift_right_click_actions"));

        // Parse view requirements
        List<Requirement> viewRequirements = parseViewRequirements(node.node("view_requirements"));

        // Parse click requirements
        Map<String, RequirementWrapper> clickRequirements = parseRequirements(node.node("click_requirements"));

        return new MenuItem(
                id,
                slot,
                priority,
                itemWrapper,
                actions.isEmpty() ? null : actions,
                rightClickActions.isEmpty() ? null : rightClickActions,
                leftClickActions.isEmpty() ? null : leftClickActions,
                middleClickActions.isEmpty() ? null : middleClickActions,
                leftShiftClickActions.isEmpty() ? null : leftShiftClickActions,
                rightShiftClickActions.isEmpty() ? null : rightShiftClickActions,
                viewRequirements.isEmpty() ? null : viewRequirements,
                clickRequirements
        );
    }

    private ItemSlot parseSlot(CommentedConfigurationNode node) {
        CommentedConfigurationNode slotNode = node.node("slot");
        CommentedConfigurationNode slotsNode = node.node("slots");

        if (!slotNode.virtual()) {
            try {
                if (slotNode.isMap()) {
                    // Complex slot configuration - not implemented yet
                    return ItemSlot.of(Collections.singletonList(0));
                } else if (slotNode.isList()) {
                    List<Integer> slots = slotNode.getList(Integer.class);
                    if (slots != null && !slots.isEmpty()) {
                        return ItemSlot.of(slots);
                    }
                } else {
                    int slot = slotNode.getInt();
                    return ItemSlot.of(Collections.singletonList(slot));
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to parse slot: " + e.getMessage());
            }
        } else if (!slotsNode.virtual()) {
            try {
                if (slotsNode.isList()) {
                    List<Integer> slots = slotsNode.getList(Integer.class);
                    if (slots != null && !slots.isEmpty()) {
                        return ItemSlot.of(slots);
                    }
                } else {
                    // Parse as comma-separated string
                    String slotsStr = slotsNode.getString();
                    if (slotsStr != null) {
                        List<Integer> slots = Arrays.stream(slotsStr.split(","))
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        return ItemSlot.of(slots);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to parse slots: " + e.getMessage());
            }
        }

        return null;
    }

    private ItemWrapper parseItemWrapper(CommentedConfigurationNode node) {
        // Parse material
        CommentedConfigurationNode materialNode = node.node("material");
        if (materialNode.virtual()) {
            materialNode = node.node("item");
        }

        if (materialNode.virtual()) {
            return null;
        }

        String materialStr = materialNode.getString();
        if (materialStr == null) {
            return null;
        }

        Material material;
        try {
            material = Material.valueOf(materialStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Unknown material: " + materialStr);
            return null;
        }


        ItemWrapper.ItemWrapperBuilder builder = ItemWrapper.builder();
        ItemStack item = new ItemStack(material);

        // Parse amount
        CommentedConfigurationNode amountNode = node.node("amount");
        if (!amountNode.virtual()) {
            item.setAmount(amountNode.getInt(1));
        }

        // Parse display name
        CommentedConfigurationNode nameNode = node.node("display_name");
        if (nameNode.virtual()) {
            nameNode = node.node("name");
        }
        if (!nameNode.virtual()) {
            String name = nameNode.getString();
            if (name != null) {
                builder.displayName(name);
            }
        }

        // Parse lore
        CommentedConfigurationNode loreNode = node.node("lore");
        if (!loreNode.virtual() && loreNode.isList()) {
            try {
                List<String> loreStrings = loreNode.getList(String.class);
                if (loreStrings != null && !loreStrings.isEmpty()) {
                    builder.lore(loreStrings);
                }
            } catch (SerializationException e) {
                plugin.getLogger().warning("Failed to parse lore: " + e.getMessage());
            }
        }

        // Parse model data
        CommentedConfigurationNode modelNode = node.node("model-data");
        if (modelNode.virtual()) {
            modelNode = node.node("custom_model_data");
        }
        if (!modelNode.virtual()) {
            CommentedConfigurationNode finalModelNode = modelNode;
            item.editMeta(m -> m.setCustomModelData(finalModelNode.getInt()));
        }

        // Parse NBT
        CommentedConfigurationNode nbtNode = node.node("nbt");
        if (!nbtNode.virtual()) {
            String nbt = nbtNode.getString();
            if (nbt != null && !nbt.isEmpty()) {
                // TODO: Parse NBT string to actual NBT data (requires NBT library
                //                builder.nbt(nbt);
            }
        }

        return builder
                .itemStack(item)
                .build();
    }

    private List<Action> parseActions(CommentedConfigurationNode node) {
        List<Action> actions = new ArrayList<>();

        if (node.virtual()) {
            return actions;
        }

        if (node.isList()) {
            try {
                List<String> actionStrings = node.getList(String.class);
                if (actionStrings != null) {
                    for (String actionStr : actionStrings) {
                        String converted = convertDeluxeMenuAction(actionStr);
                        if (converted != null) {
                            try {
                                // Create a temporary node with the converted action string
                                CommentedConfigurationNode tempNode = CommentedConfigurationNode.root();
                                tempNode.set(converted);
                                
                                // Parse the action using the MenuComponentTypeSerializer
                                MenuComponentTypeSerializer<Action> serializer = 
                                    new MenuComponentTypeSerializer<>(Registries.ACTION, Registries.ACTION_ADAPTER);
                                Action action = serializer.deserialize(Action.class, tempNode);
                                if (action != null) {
                                    actions.add(action);
                                    plugin.getLogger().fine("Successfully parsed action: " + converted);
                                }
                            } catch (SerializationException e) {
                                plugin.getLogger().warning("Failed to parse action '" + actionStr + "': " + e.getMessage());
                            }
                        }
                    }
                }
            } catch (SerializationException e) {
                plugin.getLogger().warning("Failed to parse actions: " + e.getMessage());
            }
        }

        return actions;
    }

    private List<Requirement> parseViewRequirements(CommentedConfigurationNode node) {
        List<Requirement> requirements = new ArrayList<>();

        if (node.virtual()) {
            return requirements;
        }

        if (node.isList()) {
            try {
                List<String> requirementStrings = node.getList(String.class);
                if (requirementStrings != null) {
                    for (String reqStr : requirementStrings) {
                        String converted = convertDeluxeMenuRequirement(reqStr);
                        if (converted != null) {
                            try {
                                CommentedConfigurationNode tempNode = CommentedConfigurationNode.root();
                                tempNode.set(converted);
                                
                                MenuComponentTypeSerializer<Requirement> serializer = 
                                    new MenuComponentTypeSerializer<>(Registries.REQUIREMENT, Registries.REQUIREMENT_ADAPTER);
                                Requirement requirement = serializer.deserialize(Requirement.class, tempNode);
                                if (requirement != null) {
                                    requirements.add(requirement);
                                    plugin.getLogger().fine("Successfully parsed requirement: " + converted);
                                }
                            } catch (SerializationException e) {
                                plugin.getLogger().warning("Failed to parse requirement '" + reqStr + "': " + e.getMessage());
                            }
                        }
                    }
                }
            } catch (SerializationException e) {
                plugin.getLogger().warning("Failed to parse view requirements: " + e.getMessage());
            }
        }

        return requirements;
    }

    private Map<String, RequirementWrapper> parseRequirements(CommentedConfigurationNode node) {
        Map<String, RequirementWrapper> requirements = new HashMap<>();

        if (node.virtual()) {
            return requirements;
        }

        node.childrenMap().forEach((key, reqNode) -> {
            try {
                CommentedConfigurationNode requirementNode = reqNode.node("requirement");
                CommentedConfigurationNode denyActionsNode = reqNode.node("deny_actions");

                if (!requirementNode.virtual()) {
                    String reqStr = requirementNode.getString();
                    if (reqStr != null) {
                        String converted = convertDeluxeMenuRequirement(reqStr);
                        if (converted != null) {
                            // Parse deny actions
                            List<Action> denyActions = parseActions(denyActionsNode);

                            plugin.getLogger().info("Parsed requirement " + key + ": " + converted);
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to parse requirement " + key + ": " + e.getMessage());
            }
        });

        return requirements;
    }

    private String convertDeluxeMenuAction(String deluxeAction) {
        if (deluxeAction == null || deluxeAction.isEmpty()) {
            return null;
        }

        // DeluxeMenus actions typically use format: [action_type] data
        // Most VulkanMenu actions use the same format
        
        // Direct mappings (already compatible)
        if (deluxeAction.startsWith("[message]") ||
            deluxeAction.startsWith("[player]") ||
            deluxeAction.startsWith("[console]") ||
            deluxeAction.startsWith("[close]") ||
            deluxeAction.startsWith("[sound]") ||
            deluxeAction.startsWith("[broadcast]") ||
            deluxeAction.startsWith("[title]") ||
            deluxeAction.startsWith("[teleport]")) {
            return deluxeAction;  // These are already correct
        }
        
        // Actions that need conversion
        if (deluxeAction.startsWith("[openguimenu]")) {
            String menuName = deluxeAction.replace("[openguimenu]", "").trim();
            return "[open-menu] " + menuName;
        } else if (deluxeAction.startsWith("[actionbar]")) {
            return deluxeAction.replace("[actionbar]", "[action-bar]");
        } else if (deluxeAction.startsWith("[connect]")) {
            // Server connection - not directly supported in VulkanMenu
            plugin.getLogger().warning("Server connection action not supported: " + deluxeAction);
            return null;
        }

        // If no mapping found, try to use it as-is (might be a custom action)
        plugin.getLogger().fine("Using unknown action as-is: " + deluxeAction);
        return deluxeAction;
    }

    private String convertDeluxeMenuRequirement(String deluxeRequirement) {
        if (deluxeRequirement == null || deluxeRequirement.isEmpty()) {
            return null;
        }

        // Common requirement mappings
        if (deluxeRequirement.startsWith("has permission:")) {
            String permission = deluxeRequirement.replace("has permission:", "").trim();
            return "[permission] " + permission;
        } else if (deluxeRequirement.startsWith("has money:")) {
            // Vault economy requirement - would need vault hook
            return null;
        } else if (deluxeRequirement.startsWith("has level:")) {
            String level = deluxeRequirement.replace("has level:", "").trim();
            return "[experience] " + level;
        } else if (deluxeRequirement.startsWith("string equals:")) {
            // String comparison requirement
            return null;
        } else if (deluxeRequirement.startsWith("string contains:")) {
            String value = deluxeRequirement.replace("string contains:", "").trim();
            return "[contains] " + value;
        } else if (deluxeRequirement.startsWith("regex matches:")) {
            String pattern = deluxeRequirement.replace("regex matches:", "").trim();
            return "[regex] " + pattern;
        }

        // If no mapping found, log it
        plugin.getLogger().warning("Unknown DeluxeMenus requirement type: " + deluxeRequirement);
        return null;
    }

    @Override
    public String pluginName() {
        return "DeluxeMenus";
    }

    @Override
    public Path dataFolder() {
        return this.plugin.getDataPath()
                .getParent()
                .resolve(this.pluginName());
    }

    @Override
    public Path menusFolder() {
        return this.dataFolder()
                .resolve("gui_menus");
    }
}