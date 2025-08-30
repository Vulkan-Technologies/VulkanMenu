package com.vulkantechnologies.menu.hook.implementation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;
import com.vulkantechnologies.menu.model.provider.ItemsAdderItemStackProvider;
import com.vulkantechnologies.menu.registry.Registries;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemsAdderPluginHook implements PluginHook, PlaceholderProcessor {

    private static final Pattern pattern = Pattern.compile(":(\\w+):");
    private final VulkanMenu plugin;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into ItemsAdder!");

        this.plugin.placeholderProcessors().add(this);
        Registries.ITEM_PROVIDERS.register(new ItemsAdderItemStackProvider());
    }

    @Override
    public String pluginName() {
        return "ItemsAdder";
    }

    @Override
    public String process(Player player, String content) {
        Matcher matcher = pattern.matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String replacement = FontImageWrapper.replaceFontImages(matcher.group(0));
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
