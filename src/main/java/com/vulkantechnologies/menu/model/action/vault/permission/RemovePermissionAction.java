package com.vulkantechnologies.menu.model.action.vault.permission;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.hook.implementation.VaultPluginHook;
import com.vulkantechnologies.menu.model.action.HookAction;
import com.vulkantechnologies.menu.model.menu.Menu;

@ComponentName("remove-permission")
public class RemovePermissionAction extends HookAction<VaultPluginHook> {

    private final String permission;

    public RemovePermissionAction(String permission) {
        super(VaultPluginHook.class);
        this.permission = permission;
    }

    @Override
    protected void performAction(Player player, Menu menu, VaultPluginHook hook) {
        hook.permission().playerRemove(player, this.permission);
    }

}
