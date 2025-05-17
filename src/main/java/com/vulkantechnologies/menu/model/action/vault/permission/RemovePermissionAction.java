package com.vulkantechnologies.menu.model.action.vault.permission;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.hook.implementation.VaultPluginHook;
import com.vulkantechnologies.menu.model.action.HookAction;

@ComponentName("remove-permission")
public class RemovePermissionAction extends HookAction<VaultPluginHook> {

    private final String permission;

    public RemovePermissionAction(String permission) {
        super(VaultPluginHook.class);
        this.permission = permission;
    }

    @Override
    protected void performAction(Player player, VaultPluginHook hook) {
        hook.permission().playerRemove(player, this.permission);
    }

}
