package com.vulkantechnologies.menu.command.completion;

import java.util.Collection;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.importer.ConfigurationImporter;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImporterCompletionHandler implements CommandCompletions.AsyncCommandCompletionHandler<BukkitCommandCompletionContext> {

    private final VulkanMenu plugin;

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return this.plugin.importService()
                .availableImporters()
                .stream()
                .map(ConfigurationImporter::pluginName)
                .toList();
    }
}
