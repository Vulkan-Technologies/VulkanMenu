package com.vulkantechnologies.menu.model.action;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class HookAction<T extends PluginHook> implements Action {

    private final Class<T> hookClass;

    @Override
    public void accept(Player player) {
        VulkanMenu.get()
                .pluginHooks()
                .hook(this.hookClass)
                .ifPresent(hook -> this.performAction(player, hook));
    }

    protected abstract void performAction(Player player, T hook);
}
