package com.vulkantechnologies.menu.hook.implementation;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;

@RequiredArgsConstructor
public class PlaceholderAPIPluginHook implements PluginHook, PlaceholderProcessor {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into PlaceholderAPI!");

        this.plugin.placeholderProcessors().add(this);
    }

    @Override
    public String pluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public String process(Player player, String content) {
        return PlaceholderAPI.setPlaceholders(player, content);
    }
}
