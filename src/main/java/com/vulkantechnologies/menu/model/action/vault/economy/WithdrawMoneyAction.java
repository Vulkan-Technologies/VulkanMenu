package com.vulkantechnologies.menu.model.action.vault.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.hook.implementation.VaultPluginHook;
import com.vulkantechnologies.menu.model.action.HookAction;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.milkbowl.vault.economy.Economy;

@ComponentName("withdraw-money")
public class WithdrawMoneyAction extends HookAction<VaultPluginHook> {

    private final double amount;
    private final @Nullable String currency;

    public WithdrawMoneyAction(double amount) {
        this(amount, null);
    }

    public WithdrawMoneyAction(double amount, @Nullable String currency) {
        super(VaultPluginHook.class);
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    protected void performAction(Player player, Menu menu, VaultPluginHook hook) {
        Economy economy = hook.economy();

        if (currency != null) {
            economy.withdrawPlayer(player, currency, amount);
            return;
        }

        economy.withdrawPlayer(player, amount);
    }

}
