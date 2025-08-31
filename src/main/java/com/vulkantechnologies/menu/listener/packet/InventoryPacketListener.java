package com.vulkantechnologies.menu.listener.packet;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
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
                packet.setStateId(menu.incrementStateId());
                packet.setItems(Arrays.stream(menu.cachedItems())
                        .map(SpigotConversionUtil::fromBukkitItemStack)
                        .toList());
                event.markForReEncode(true);
            });
        } else if (event.getPacketType().equals(PacketType.Play.Server.SET_SLOT)) {
            WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event);

            int slot = packet.getSlot();
            this.handle(player, menu -> {
                if (slot >= menu.cachedItems().length || slot < menu.configuration().size())
                    return;

                packet.setStateId(menu.stateId());
                event.markForReEncode(true);

                menu.getShownItem(slot).ifPresent(item -> {
                    ItemStack cachedItem = menu.cachedItems()[slot];
                    if (cachedItem == null)
                        return;

                    packet.setItem(SpigotConversionUtil.fromBukkitItemStack(cachedItem));
                });
            });
        } else if (event.getPacketType().equals(PacketType.Play.Server.OPEN_WINDOW)) {
            WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(event);
            this.handle(player, menu -> menu.windowId(packet.getContainerId()));
        }
    }

    @Override
    public void onPacketPlayReceive(@NotNull PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();

        if (event.getPacketType().equals(PacketType.Play.Client.WINDOW_CONFIRMATION)) {
            this.handleWindowConfirmation(player, event);
        }
    }


    private void handleWindowConfirmation(Player player, PacketPlayReceiveEvent event) {
        this.handle(player, menu -> {
            event.setCancelled(true);
            WrapperPlayClientWindowConfirmation confirmationPacket = new WrapperPlayClientWindowConfirmation(event);

            WrapperPlayServerWindowConfirmation resend =
                    new WrapperPlayServerWindowConfirmation(
                            confirmationPacket.getWindowId(),
                            confirmationPacket.getActionId(),
                            true
                    );
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, resend);
        });
    }

    private void handle(Player player, Consumer<Menu> menuConsumer) {
        this.plugin.menu()
                .findByPlayer(player)
                .ifPresent(menuConsumer);
    }
}
