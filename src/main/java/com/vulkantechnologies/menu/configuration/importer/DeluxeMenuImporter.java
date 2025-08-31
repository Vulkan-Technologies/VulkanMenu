package com.vulkantechnologies.menu.configuration.importer;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
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
import com.vulkantechnologies.menu.model.provider.ItemStackProvider;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.ExperienceRequirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.HasMetaRequirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.IsNearRequirement;
import com.vulkantechnologies.menu.model.requirement.minecraft.PermissionRequirement;
import com.vulkantechnologies.menu.model.requirement.vault.MoneyRequirement;
import com.vulkantechnologies.menu.model.requirement.vulkan.CompareRequirement;
import com.vulkantechnologies.menu.model.requirement.vulkan.ContainsRequirement;
import com.vulkantechnologies.menu.model.requirement.vulkan.RegexRequirement;
import com.vulkantechnologies.menu.model.requirement.vulkan.StringLengthRequirement;
import com.vulkantechnologies.menu.model.wrapper.ComponentWrapper;
import com.vulkantechnologies.menu.model.wrapper.ItemWrapper;
import com.vulkantechnologies.menu.model.wrapper.RequirementWrapper;
import com.vulkantechnologies.menu.registry.Registries;
import com.vulkantechnologies.menu.utils.ComponentUtils;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;

@RequiredArgsConstructor
public class DeluxeMenuImporter implements ConfigurationImporter {


    private final VulkanMenu plugin;

    @Override
    public MenuConfiguration process(CommentedConfigurationNode node) {
        MenuConfiguration.MenuConfigurationBuilder builder = MenuConfiguration.builder()
                .variables(new HashMap<>());

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
            builder.title(new ComponentWrapper(ComponentUtils.legacyToMiniMessage(title)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse title", e);
        }

        // Open command
        CommentedConfigurationNode openCommandNode = node.node("open_command");
        if (!openCommandNode.virtual()) {
            CommandConfiguration.CommandConfigurationBuilder commandBuilder = CommandConfiguration.builder();

            if (openCommandNode.isList()) {
                try {
                    List<String> commands = openCommandNode.getList(String.class);
                    if (commands != null && !commands.isEmpty()) {
                        String firstCommand = commands.get(0);
                        commandBuilder.name(firstCommand);

                        if (commands.size() > 1)
                            commandBuilder.aliases(commands.subList(1, commands.size()));
                    }
                } catch (SerializationException e) {
                    throw new RuntimeException("Failed to parse open_command list", e);
                }
            } else {
                String openCommand = openCommandNode.getString();
                if (openCommand != null && !openCommand.isEmpty())
                    commandBuilder.name(openCommand);
            }

            builder.openCommand(commandBuilder.build());
        }

        // Open actions
        CommentedConfigurationNode openActionNode = node.node("open_commands");
        if (!openActionNode.virtual())
            builder.openActions(parseActions(openActionNode));

        // Open requirements
        CommentedConfigurationNode openRequirementsNode = node.node("open_requirement");
        if (!openRequirementsNode.virtual())
            builder.openRequirements(parseRequirements(openRequirementsNode));

        // Close actions
        CommentedConfigurationNode closeActionsNode = node.node("close_actions");
        if (!closeActionsNode.virtual())
            builder.closeActions(parseActions(closeActionsNode));

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
                    plugin.getSLF4JLogger().warn("Failed to parse item {}: ", key, e);
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
            plugin.getSLF4JLogger().warn("Item '{}' has no valid slot", id);
            return null;
        }

        // Parse item wrapper
        ItemWrapper itemWrapper = parseItemWrapper(node);
        if (itemWrapper == null) {
            plugin.getSLF4JLogger().warn("Item '{}' has no valid material", id);
            return null;
        }

        // Parse priority
        int priority = node.node("priority").getInt(0);

        // Parse actions for different click types
        List<Action> actions = parseActions(node.node("actions"));
        List<Action> leftClickActions = parseActions(node.node("left_click_commands"));
        List<Action> rightClickActions = parseActions(node.node("right_click_commands"));
        List<Action> middleClickActions = parseActions(node.node("middle_click_commands"));
        List<Action> leftShiftClickActions = parseActions(node.node("shift_left_click_commands"));
        List<Action> rightShiftClickActions = parseActions(node.node("shift_right_click_commands"));

        // Parse view requirements
        Map<String, RequirementWrapper> viewRequirements = parseRequirements(node.node("view_requirement"));

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
                viewRequirements.isEmpty() ? null : viewRequirements.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(RequirementWrapper::requirement)
                        .collect(Collectors.toList()),
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
                plugin.getSLF4JLogger().warn("Failed to parse slot: ", e);
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
                plugin.getSLF4JLogger().warn("Failed to parse slots: ", e);
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

        String rawMaterial = materialNode.getString();
        if (rawMaterial == null)
            return null;

        ItemStack item;
        if (rawMaterial.contains("-")) {
            String[] materialParts = rawMaterial.split("-");
            ItemStackProvider provider;
            try {
                String rawProvider = materialParts[0];
                provider = Registries.ITEM_PROVIDERS.findByPrefix(rawProvider)
                        .orElseThrow(() -> new SerializationException("Invalid item provider: " + materialParts[0]));
                item = provider.provide(materialParts[1]);
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        } else {
            Material material = Registry.MATERIAL.getOrThrow(Key.key(materialNode.getString().toLowerCase()));
            item = new ItemStack(material);
        }

        ItemWrapper.ItemWrapperBuilder builder = ItemWrapper.builder();

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
                builder.displayName(ComponentUtils.legacyToMiniMessage(name));
            }
        }

        // Parse lore
        CommentedConfigurationNode loreNode = node.node("lore");
        if (!loreNode.virtual() && loreNode.isList()) {
            try {
                List<String> loreStrings = loreNode.getList(String.class);
                if (loreStrings != null && !loreStrings.isEmpty()) {
                    builder.lore(loreStrings.stream()
                            .map(ComponentUtils::legacyToMiniMessage)
                            .collect(Collectors.toList()));
                }
            } catch (SerializationException e) {
                plugin.getSLF4JLogger().warn("Failed to parse lore: ", e);
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

        if (node.virtual() || !node.isList())
            return actions;

        try {
            List<String> actionStrings = node.getList(String.class);
            if (actionStrings == null)
                return actions;

            for (String actionStr : actionStrings) {
                String converted = convertDeluxeMenuAction(actionStr);
                if (converted == null)
                    continue;

                converted = ComponentUtils.legacyToMiniMessage(converted);

                try {
                    // Create a temporary node with the converted action string
                    CommentedConfigurationNode tempNode = CommentedConfigurationNode.root();
                    tempNode.set(converted);

                    // Parse the action using the MenuComponentTypeSerializer
                    MenuComponentTypeSerializer<Action> serializer = new MenuComponentTypeSerializer<>(Registries.ACTION, Registries.ACTION_ADAPTER);
                    Action action = serializer.deserialize(Action.class, tempNode);
                    if (action == null)
                        continue;

                    actions.add(action);
                    plugin.getLogger().fine("Successfully parsed action: " + converted);
                } catch (SerializationException e) {
                    plugin.getSLF4JLogger().warn("Failed to parse action '{}': ", actionStr, e);
                }
            }
        } catch (SerializationException e) {
            plugin.getSLF4JLogger().warn("Failed to parse actions: ", e);
        }

        return actions;
    }

    private Map<String, RequirementWrapper> parseRequirements(CommentedConfigurationNode node) {
        Map<String, RequirementWrapper> requirements = new HashMap<>();
        if (node.virtual())
            return requirements;

        CommentedConfigurationNode requirementsNode = node.node("requirements");
        if (requirementsNode.childrenMap().isEmpty())
            return requirements;

        for (Map.Entry<Object, CommentedConfigurationNode> entry : requirementsNode.childrenMap().entrySet()) {
            String key = entry.getKey().toString();
            CommentedConfigurationNode requirementNode = entry.getValue();

            String type = requirementNode.node("type").getString();
            Requirement requirement = switch (type) {
                case "has permission" -> new PermissionRequirement(requirementNode.node("permission").getString());
                case "has money" -> new MoneyRequirement(requirementNode.node("amount").getDouble(0));
                case "has meta" ->
                        new HasMetaRequirement(requirementNode.node("key").getString(), requirementNode.node("meta_type").getString("STRING"));
                case "has exp" -> new ExperienceRequirement(requirementNode.node("amount").getInt(0));
                case "is near" -> {
                    String rawLocation = requirementNode.node("location").getString();
                    double distance = requirementNode.node("distance").getDouble(0);

                    Location location;
                    String[] parts = rawLocation != null ? rawLocation.split(",") : new String[0];
                    if (parts.length == 4) {
                        String worldName = parts[0].trim();
                        double x = Double.parseDouble(parts[1].trim());
                        double y = Double.parseDouble(parts[2].trim());
                        double z = Double.parseDouble(parts[3].trim());
                        location = new Location(plugin.getServer().getWorld(worldName), x, y, z);
                    } else
                        throw new RuntimeException("Invalid location: " + rawLocation);

                    yield new IsNearRequirement(location, distance);
                }
                case "string equals" -> {
                    String input = requirementNode.node("input").getString();
                    String compareTo = requirementNode.node("output").getString();

                    yield new CompareRequirement("%s == %s".formatted(input, compareTo));
                }
                case "string contains" ->
                        new ContainsRequirement(requirementsNode.node("input").getString(), requirementNode.node("output").getString());
                case "string length" ->
                        new StringLengthRequirement(requirementsNode.node("min").getInt(0), requirementsNode.node("max").getInt(Integer.MAX_VALUE), requirementNode.node("input").getString());
                case "regex matches" ->
                        new RegexRequirement(node.node("regex").getString(), requirementNode.node("input").getString());
                case "==" ->
                        new CompareRequirement("%s == %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                case "!=" ->
                        new CompareRequirement("%s != %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                case ">=", "=>" ->
                        new CompareRequirement("%s >= %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                case "<=", "=<" ->
                        new CompareRequirement("%s <= %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                case ">" ->
                        new CompareRequirement("%s > %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                case "<" ->
                        new CompareRequirement("%s < %s".formatted(requirementNode.node("input").getString(), requirementNode.node("output").getString()));
                default -> {
                    this.plugin.getSLF4JLogger().warn("Unknown requirement type: {}", type);
                    yield null;
                    // throw new IllegalArgumentException("Invalid requirement type: " + type);
                }
            };
            if (requirement == null)
                continue;

            CommentedConfigurationNode denyActionsNode = requirementNode.node("deny_commands");
            requirements.put(key, new RequirementWrapper(requirement, parseActions(denyActionsNode)));
        }

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
            deluxeAction.startsWith("[teleport]"))
            return deluxeAction;  // These are already correct

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
        } else if (deluxeAction.startsWith("[takemoney]")) {
            String amountStr = deluxeAction.replace("[takemoney]", "").trim();
            return "[withdraw-money] " + amountStr;
        } else if (deluxeAction.startsWith("[givemoney]")) {
            String amountStr = deluxeAction.replace("[givemoney]", "").trim();
            return "[deposit-money] " + amountStr;
        }

        // If no mapping found, try to use it as-is (might be a custom action)
        plugin.getLogger().fine("Using unknown action as-is: " + deluxeAction);
        return deluxeAction;
    }

    @Override
    public String pluginName() {
        return "DeluxeMenus";
    }

    @Override
    public Path dataFolder() {
        return this.plugin.getDataFolder()
                .toPath()
                .resolve("menus")
                .resolve(this.pluginName());
    }

    @Override
    public Path menusFolder() {
        return this.plugin.getDataFolder()
                .toPath()
                .getParent()
                .resolve(this.pluginName())
                .resolve("gui_menus");
    }
}