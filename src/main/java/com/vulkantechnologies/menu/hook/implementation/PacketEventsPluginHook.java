package com.vulkantechnologies.menu.hook.implementation;

import com.github.retrooper.packetevents.PacketEvents;
import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.listener.packet.InventoryPacketListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketEventsPluginHook implements PluginHook {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("PacketEvents plugin hook loaded successfully.");

        PacketEvents.getAPI().getEventManager().registerListener(new InventoryPacketListener(this.plugin));
    }

    @Override
    public void onFailure() {
        this.plugin.getSLF4JLogger().warn("PacketEvents plugin hook failed to load.");
    }

    @Override
    public String pluginName() {
        return "packetevents";
    }
}
