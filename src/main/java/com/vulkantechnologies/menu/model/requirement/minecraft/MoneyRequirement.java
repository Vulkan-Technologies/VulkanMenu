package com.vulkantechnologies.menu.model.requirement.minecraft;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.hook.implementation.VaultPluginHook;
import com.vulkantechnologies.menu.model.requirement.Requirement;

@ComponentName("money")
public record MoneyRequirement(double amount) implements Requirement {

    @Override
    public boolean test(Player player) {
        return VulkanMenu.get()
                .pluginHooks()
                .hook(VaultPluginHook.class)
                .map(hook -> hook.economy().has(player, amount))
                .orElse(false);
    }

}
