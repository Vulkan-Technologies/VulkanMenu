# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
VulkanMenu is a Minecraft Bukkit/Paper plugin for creating and managing custom GUI menus. It supports dynamic content with placeholders, integrations with popular plugins (Vault, PlaceholderAPI, ItemsAdder, Oraxen, Nexo), and a packet-based system for enhanced performance.

## Build Commands
```bash
# Build and package the plugin
mvn clean package

# Build and skip tests
mvn clean package -DskipTests

# Build and deploy to test server (configured in pom.xml)
mvn clean package
# The plugin is automatically copied to server/plugins/VulkanMenu.jar

# Run with specific profile
mvn clean package -P<profile>
```

## Development Commands
```bash
# Start the test Paper server
cd server && java -jar paper-1.21.4-230.jar

# Reload plugin configuration in-game
/vmenu reload

# Open a menu in-game
/vmenu open <menu_name> [player]
```

## Architecture Overview

### Core Service Layer
- **ConfigurationService**: Manages YAML config loading using Configurate library
- **MenuService**: Handles menu creation, opening, and state management
- **PluginHookService**: Manages integrations with external plugins
- **FileWatcherService**: Hot-reloads configurations when files change
- **UpdateService**: Checks for plugin updates

### Menu System Architecture
1. **Menu Configuration**: YAML files in `resources/configuration/menus/` define menu structure
2. **Menu Components**: Modular system for menu elements (items, titles, etc.)
3. **Action System**: Extensible action framework in `model/action/` with categories:
   - minecraft/: Native Minecraft actions (messages, commands, sounds)
   - vault/: Economy and permission actions
   - vulkan/: Menu-specific actions (open, refresh, variables)
4. **Requirement System**: Conditional display/interaction based on permissions, variables

### Packet-Based Features
- Uses PacketEvents library for enhanced performance
- Packet listener in `listener/packet/InventoryPacketListener.java`

### Key Integration Points
- **PlaceholderAPI**: Dynamic content replacement
- **Vault**: Economy and permissions
- **ItemsAdder/Oraxen/Nexo**: Custom item support
- **HeadDatabase**: Custom head items
- **PacketEvents**: Packet manipulation

### Configuration Structure
```yaml
# Menu configuration example (menus/*.yml)
title: "Menu Title"
size: 27
open-command:
  name: "command"
items:
  <id>:
    slot: 0
    material: "DIAMOND"
    actions:
      - "[action] parameters"
    requirements:
      - "[requirement] condition"
```

### Plugin Lifecycle
1. **Bootstrap** (`VulkanMenuBootstrap`): Loads dependencies
2. **Loader** (`VulkanMenuLoader`): Configures classloader
3. **Main Plugin** (`VulkanMenu`): Initializes services, registers listeners
4. **Services**: Start configuration watching, menu management, hook detection

### Important Patterns
- Uses Lombok for boilerplate reduction (@Getter, @Setter, etc.)
- ACF (Annotation Command Framework) for command handling
- Configurate for YAML serialization with custom type serializers
- Service-oriented architecture with clear separation of concerns
- Registry pattern for actions, components, and adapters

### Testing
No unit tests currently exist in the project. Manual testing is done via the included Paper server in the `server/` directory.

## Key Files and Locations
- Main plugin class: `src/main/java/com/vulkantechnologies/menu/VulkanMenu.java`
- Menu configurations: `src/main/resources/configuration/menus/`
- Action implementations: `src/main/java/com/vulkantechnologies/menu/model/action/`
- Component system: `src/main/java/com/vulkantechnologies/menu/model/component/`
- Configuration serializers: `src/main/java/com/vulkantechnologies/menu/configuration/serializer/`
- Test server: `server/` directory with Paper 1.21.4
- Documentation: `../Documentation` directory