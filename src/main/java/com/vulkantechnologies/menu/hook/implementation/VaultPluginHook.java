package com.vulkantechnologies.menu.hook.implementation;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.requirement.minecraft.MoneyRequirement;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;

@RequiredArgsConstructor
@Getter
public class VaultPluginHook implements PluginHook {

    private final VulkanMenu plugin;
    private Economy economy;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into Vault!");

        this.setupEconomy();

        Registries.REQUIREMENT.register(MoneyRequirement.class);
    }

    @Override
    public void onFailure() {
        this.plugin.getSLF4JLogger().error("Failed to hook into Vault!");
        this.plugin.getSLF4JLogger().warn("\"money\" requirements will not work!");
    }

    @Override
    public String pluginName() {
        return "Vault";
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            this.plugin.getSLF4JLogger().error("No economy provider found!");
            return;
        }

        this.economy = rsp.getProvider();
    }
}
