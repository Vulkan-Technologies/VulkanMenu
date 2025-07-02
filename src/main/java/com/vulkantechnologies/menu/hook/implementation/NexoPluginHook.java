package com.vulkantechnologies.menu.hook.implementation;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.provider.OraxeItemStackProvider;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NexoPluginHook implements PluginHook {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into Nexo!");

        Registries.ITEM_PROVIDERS.register(new OraxeItemStackProvider());
    }

    @Override
    public String pluginName() {
        return "Nexo";
    }
}
