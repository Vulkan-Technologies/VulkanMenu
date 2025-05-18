package com.vulkantechnologies.menu.model.wrapper;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.minimessage.MiniMessage;

public record ItemWrapper(ItemStack itemStack, String displayName, List<String> lore) {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public ItemStack build(Player player, Menu menu) {
        ItemStack item = this.itemStack.clone();

        item.editMeta(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                String formattedName = format(player, this.displayName);
                itemMeta.displayName(MINI_MESSAGE.deserialize(formattedName, menu.variableResolver()));
            }

            if (itemMeta.hasLore()) {
                List<String> formattedLore = this.lore.stream()
                        .map(line -> format(player, line))
                        .toList();

                itemMeta.lore(formattedLore.stream()
                        .map(line -> MINI_MESSAGE.deserialize(line, menu.variableResolver()))
                        .toList());
            }
        });

        return item;
    }

    private String format(Player player, String text) {
        return VulkanMenu.get().processPlaceholders(player, text);
    }
}
