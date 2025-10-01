package com.vulkantechnologies.menu.hook.implementation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.listener.packet.InventoryPacketListener;
import com.vulkantechnologies.menu.model.menu.Menu;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
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
    public String pluginName() {
        return "packetevents";
    }

    public void sendItemUpdate(Player player, Menu menu, int slot, ItemStack item) {
        WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(menu.windowId(), menu.incrementStateId(), slot, SpigotConversionUtil.fromBukkitItemStack(item));
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    public void setTitle(Player player, Menu menu) {
        int type = switch (menu.inventory().getSize()) {
            case 9 -> 0;
            case 18 -> 1;
            case 27 -> 2;
            case 36 -> 3;
            case 45 -> 4;
            case 54 -> 5;
            default -> throw new IllegalArgumentException("Invalid slot size: " + menu.inventory().getSize());
        };

        WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(menu.windowId(), type, menu.buildTitle(player));
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

}
