package com.vulkantechnologies.menu.placeholder;

import java.util.Map;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.model.PlaceholderProcessor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalVariablePlaceholderProcessor implements PlaceholderProcessor {

    private final VulkanMenu plugin;

    @Override
    public String process(Player player, String content) {
        for (Map.Entry<String, String> entry : this.plugin.mainConfiguration().globalVariables().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            content = content.replace("<" + key + ">", value);
        }

        return content;
    }
}
