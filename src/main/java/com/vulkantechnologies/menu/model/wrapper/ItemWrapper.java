package com.vulkantechnologies.menu.model.wrapper;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.utils.ItemMarker;

import lombok.Builder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Builder
public record ItemWrapper(ItemStack itemStack, String displayName, List<String> lore) {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public ItemStack build(Player player, Menu menu) {
        ItemStack item = this.itemStack.clone();

        item.editMeta(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                String formattedName = format(player, this.displayName);
                itemMeta.displayName(this.deserializeWithReset(formattedName, menu.variableResolver()));
            }

            if (itemMeta.hasLore()) {
                List<Component> formattedLore = this.lore.stream()
                        .map(line -> format(player, line))
                        .map(line -> this.deserializeWithReset(line, menu.variableResolver()))
                        .toList();

                itemMeta.lore(formattedLore);
            }
        });
        ItemMarker.mark(item);
        return item;
    }

    private Component deserializeWithReset(String text, TagResolver resolver) {
        return MINI_MESSAGE.deserialize(text, resolver)
                .decoration(TextDecoration.ITALIC, false);
    }

    private String format(Player player, String text) {
        return VulkanMenu.get().processPlaceholders(player, text);
    }
}
