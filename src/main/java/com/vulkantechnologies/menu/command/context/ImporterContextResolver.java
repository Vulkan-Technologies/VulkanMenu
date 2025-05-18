package com.vulkantechnologies.menu.command.context;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImporterContextResolver implements ContextResolver<ConfigurationImporter, BukkitCommandExecutionContext> {

    private final VulkanMenu plugin;

    @Override
    public ConfigurationImporter getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        String menuName = context.popFirstArg();

        return this.plugin.importService()
                .findByPluginName(menuName)
                .orElseThrow(() -> new InvalidCommandArgument("Menu importer not found: " + menuName));
    }
}
