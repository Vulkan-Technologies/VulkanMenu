package com.vulkantechnologies.menu.listener.packet;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.menu.Menu;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryPacketListener extends SimplePacketListenerAbstract {

    private final VulkanMenu plugin;

    @Override
    public void onPacketPlaySend(@NotNull PacketPlaySendEvent event) {
        Player player = event.getPlayer();

        if (event.getPacketType().equals(PacketType.Play.Server.WINDOW_ITEMS)) {
            WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event);
            this.handle(player, menu -> {
                packet.setItems(Arrays.stream(menu.cachedItems())
                        .map(SpigotConversionUtil::fromBukkitItemStack)
                        .toList());
                event.markForReEncode(true);
            });
        } else if (event.getPacketType().equals(PacketType.Play.Server.SET_SLOT)) {
            WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event);

            int slot = packet.getSlot();
            this.handle(player, menu -> {
                if (slot < menu.configuration().size())
                    return;

                menu.getItem(packet.getSlot())
                        .ifPresent(item -> {
                            packet.setItem(SpigotConversionUtil.fromBukkitItemStack(menu.cachedItems()[slot]));
                            event.markForReEncode(true);
                        });
            });
        }
    }

    private void handle(Player player, Consumer<Menu> menuConsumer) {
        this.plugin.menu()
                .findByPlayer(player)
                .ifPresent(menuConsumer);
    }
}
