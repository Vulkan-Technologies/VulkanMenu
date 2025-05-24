package com.vulkantechnologies.menu.command.context;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.configuration.menu.MenuConfigurationFile;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuContextResolver implements ContextResolver<MenuConfiguration, BukkitCommandExecutionContext> {

    private final VulkanMenu plugin;

    @Override
    public MenuConfiguration getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        String menuName = context.popFirstArg();

        return this.plugin.configuration()
                .findByName(menuName)
                .map(MenuConfigurationFile::menu)
                .orElseThrow(() -> new InvalidCommandArgument("Menu not found: " + menuName));
    }
}
