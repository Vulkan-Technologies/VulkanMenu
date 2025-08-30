package com.vulkantechnologies.menu.hook.implementation;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.provider.HeadDatabaseItemProvider;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeadDatabasePluginHook implements PluginHook {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into HeadDatabase!");
        Registries.ITEM_PROVIDERS.register(new HeadDatabaseItemProvider());
    }

    @Override
    public String pluginName() {
        return "HeadDatabase";
    }
}
