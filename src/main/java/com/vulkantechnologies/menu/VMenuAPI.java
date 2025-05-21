package com.vulkantechnologies.menu;

import java.util.Optional;

import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.configuration.MenuConfiguration;
import com.vulkantechnologies.menu.configuration.MenuConfigurationFile;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.adapter.CompactAdapter;
import com.vulkantechnologies.menu.model.menu.Menu;
import com.vulkantechnologies.menu.model.provider.ItemStackProvider;
import com.vulkantechnologies.menu.model.requirement.Requirement;
import com.vulkantechnologies.menu.registry.Registries;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VMenuAPI {

    private static VulkanMenu plugin;

    static void init(VulkanMenu plugin) {
        VMenuAPI.plugin = plugin;
    }

    public static void registerAction(Class<? extends Action> actionClass) {
        Registries.ACTION.register(actionClass);
    }

    public static void registerRequirement(Class<? extends Requirement> requirementClass) {
        Registries.REQUIREMENT.register(requirementClass);
    }

    public static void registerItemProvider(ItemStackProvider itemProviderClass) {
        Registries.ITEM_PROVIDERS.register(itemProviderClass);
    }

    public static void registerCompactAdapter(CompactAdapter<?> adapter) {
        Registries.COMPACT_ADAPTER.register(adapter);
    }

    public static Optional<MenuConfiguration> findMenuConfiguration(String name) {
        return plugin.configuration()
                .findByName(name)
                .map(MenuConfigurationFile::menu);
    }

    public static boolean hasOpenMenu(Player player) {
        return plugin.menu().findByPlayer(player).isPresent();
    }

    public static Optional<Menu> getOpenMenu(Player player) {
        return plugin.menu().findByPlayer(player);
    }

}
