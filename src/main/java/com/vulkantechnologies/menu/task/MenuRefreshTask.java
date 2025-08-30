package com.vulkantechnologies.menu.task;

import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRefreshTask extends BukkitRunnable {

    private final VulkanMenu plugin;

    @Override
    public void run() {
        List<Menu> menus = this.plugin.menu().menus();
        for (Menu menu : menus) {

            MenuConfiguration configuration = menu.configuration();
            MenuConfiguration.Refresh refresh = configuration.refresh();
            if (refresh == null || !refresh.isValid())
                continue;

            long currentTime = System.currentTimeMillis();
            if (refresh.hasDelay()
                && currentTime - menu.creationTime() < refresh.delay() * 50L)
                continue;

            long elapsedTimeSinceLastRefresh = currentTime - menu.lastRefreshTime();
            if (refresh.hasInterval()
                && elapsedTimeSinceLastRefresh < refresh.interval() * 50L)
                continue;

            menu.refresh();

            List<Action> actions = refresh.actions();
            if (actions != null) {
                for (Action action : actions) {
                    action.accept(menu.player(), menu);
                }
            }
        }
    }
}
