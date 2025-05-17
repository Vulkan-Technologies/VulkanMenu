package com.vulkantechnologies.menu.hook.implementation;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.hook.PluginHook;
import com.vulkantechnologies.menu.model.action.vault.economy.DepositMoneyAction;
import com.vulkantechnologies.menu.model.action.vault.economy.WithdrawMoneyAction;
import com.vulkantechnologies.menu.model.action.vault.permission.AddPermissionAction;
import com.vulkantechnologies.menu.model.action.vault.permission.RemovePermissionAction;
import com.vulkantechnologies.menu.model.requirement.minecraft.MoneyRequirement;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

@RequiredArgsConstructor
@Getter
public class VaultPluginHook implements PluginHook {

    private final VulkanMenu plugin;
    private Economy economy;
    private Permission permission;

    @Override
    public void onSuccess() {
        this.plugin.getSLF4JLogger().info("Successfully hooked into Vault!");

        this.setupEconomy();
        this.setupPermission();

        // Economy
        if (economy != null) {
            // Requirements
            Registries.REQUIREMENT.register(MoneyRequirement.class);

            // Actions
            List.of(
                    WithdrawMoneyAction.class,
                    DepositMoneyAction.class
            ).forEach(Registries.ACTION::register);
        }

        // Permission
        if (permission != null) {
            // Actions
            List.of(
                    AddPermissionAction.class,
                    RemovePermissionAction.class
            ).forEach(Registries.ACTION::register);
        }

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

    private void setupPermission() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            this.plugin.getSLF4JLogger().error("No permission provider found!");
            return;
        }

        this.permission = rsp.getProvider();
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
