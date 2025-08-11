package com.vulkantechnologies.menu.hook.implementation;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.listener.packet.InventoryPacketListener;

import com.vulkantechnologies.menu.model.menu.Menu;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@RequiredArgsConstructor
public class PacketEventsPluginHook implements PluginHook {

    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("PacketEvents plugin hook loaded successfully.");

        PacketEvents.getAPI().getEventManager().registerListener(new InventoryPacketListener(this.plugin));
    }

    @Override
    public String pluginName() {
        return "packetevents";
    }
}
