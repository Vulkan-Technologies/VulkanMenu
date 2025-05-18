package com.vulkantechnologies.menu.hook.implementation;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.provider.OraxeItemStackProvider;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OraxenPluginHook implements PluginHook {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into Oraxen!");

        Registries.ITEM_PROVIDERS.register(new OraxeItemStackProvider());
    }

    @Override
    public void onFailure() {
        this.plugin.getSLF4JLogger().warn("Oraxen plugin not found, some features may not work.");
    }

    @Override
    public String pluginName() {
        return "Oraxen";
    }
}
