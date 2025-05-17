package com.vulkantechnologies.menu.model.action.vault.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.hook.implementation.VaultPluginHook;
import com.vulkantechnologies.menu.model.action.HookAction;

import net.milkbowl.vault.economy.Economy;

@ComponentName("deposit-money")
public class DepositMoneyAction extends HookAction<VaultPluginHook> {

    private final double amount;
    private final @Nullable String currency;

    public DepositMoneyAction(double amount) {
        this(amount, null);
    }

    public DepositMoneyAction(double amount, @Nullable String currency) {
        super(VaultPluginHook.class);
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    protected void performAction(Player player, VaultPluginHook hook) {
        Economy economy = hook.economy();

        if (currency != null) {
            economy.depositPlayer(player, currency, amount);
            return;
        }

        economy.depositPlayer(player, amount);
    }

}