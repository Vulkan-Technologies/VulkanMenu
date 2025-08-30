package com.vulkantechnologies.menu.command;

import java.io.IOException;
import java.nio.file.Files;

import com.vulkantechnologies.menu.event.VMenuReloadEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.VulkanMenu;
import com.vulkantechnologies.menu.configuration.menu.MenuConfiguration;
import com.vulkantechnologies.menu.utils.DumpUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@CommandAlias("vmenu|vm")
public class VMenuCommand extends BaseCommand {

    @Dependency
    private VulkanMenu plugin;

    @Default
    @Description("Shows the VulkanMenu version and help.")
    public void onDefault(CommandSender sender) {
        sender.sendMessage(Component.text("VulkanMenu v")
                .append(Component.text(plugin.getDescription().getVersion()))
                .append(Component.text(" by Vulkan Technologies")));
        sender.sendMessage(Component.text("Use /vmenu help for a list of commands."));
    }

    @Subcommand("open")
    @Description("Opens a the specified menu to the player.")
    @CommandCompletion("@players @menus")
    @Syntax("<player> <menu>")
    @CommandPermission("vmenu.open")
    public void onOpen(Player player, OnlinePlayer target, MenuConfiguration menu) {
        this.plugin.menu().openMenu(target.getPlayer(), menu);
    }

    @Subcommand("open")
    @Description("Opens a the specified menu.")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onOpen(Player player, MenuConfiguration menu) {
        this.plugin.menu().openMenu(player, menu);
    }

    @Subcommand("list")
    @Description("Lists all available menus.")
    @CommandPermission("vmenu.list")
    public void onList(CommandSender sender) {
        this.plugin.mainConfiguration().sendMessage(sender, "menu-list-header");
        this.plugin.configuration()
                .menus()
                .values()
                .forEach(menu -> {
                    Component hoverText = Component.text("Size: ", NamedTextColor.GRAY).append(Component.text(menu.menu().size(), NamedTextColor.AQUA))
                            .appendNewline()
                            .append(Component.text("Items: ", NamedTextColor.GRAY).append(Component.text(menu.menu().items().size(), NamedTextColor.AQUA)))
                            .appendNewline()
                            .append(Component.text("Variables: ", NamedTextColor.GRAY).append(Component.text(menu.menu().variables().size(), NamedTextColor.AQUA)))
                            .appendNewline()
                            .appendNewline()
                            .append(Component.text("Click to open this menu.", NamedTextColor.GRAY));

                    this.plugin.mainConfiguration().sendMessage(
                            sender,
                            "menu-list-item",
                            Placeholder.component(
                                    "menu",
                                    Component.text(menu.id(), NamedTextColor.AQUA)
                                            .hoverEvent(HoverEvent.showText(hoverText))
                                            .clickEvent(ClickEvent.runCommand("/vmenu open " + menu.id()))
                            )
                    );
                });
    }

    @Subcommand("import")
    @Description("Import all menus from the specified importer.")
    @CommandCompletion("@importers")
    @Syntax("<importer>")
    @CommandPermission("vmenu.import")
    public void onImport(CommandSender sender, ConfigurationImporter importer) {
        sender.sendMessage(Component.text("Importing menus from " + importer.pluginName() + "..."));
        try {
            this.plugin.importService().importMenus(importer);
            sender.sendMessage(Component.text("Imported " + this.plugin.configuration().menus().size() + " menus."));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to import menus: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Subcommand("reload")
    @Description("Reloads the VulkanMenu configuration.")
    @CommandPermission("vmenu.reload")
    public void onReload(CommandSender sender) {
        this.plugin.mainConfiguration().sendMessage(sender, "reloading");
        try {
            this.plugin.configuration().load();
            this.plugin.mainConfiguration().load();

            VMenuReloadEvent reloadEvent = new VMenuReloadEvent();
            reloadEvent.callEvent();

            this.plugin.mainConfiguration().sendMessage(sender, "reload-success");
        } catch (Exception e) {
            this.plugin.mainConfiguration().sendMessage(sender, "reload-failed");
            e.printStackTrace();
        }
    }

    @Subcommand("reload")
    @Description("Reloads the specified menu.")
    @CommandPermission("vmenu.reload")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onReload(CommandSender sender, String menu) {
        TagResolver menuResolver = Placeholder.parsed("menu", menu);

        this.plugin.configuration()
                .findByName(menu)
                .ifPresentOrElse(menuConfigurationFile -> {
                    this.plugin.mainConfiguration().sendMessage(sender, "reloading-menu", menuResolver);
                    try {
                        this.plugin.configuration().load(menuConfigurationFile.path());

                        this.plugin.mainConfiguration().sendMessage(sender, "reload-menu-success", menuResolver);
                    } catch (Exception e) {
                        this.plugin.mainConfiguration().sendMessage(sender, "reload-menu-failed", menuResolver);
                        e.printStackTrace();
                    }
                }, () -> this.plugin.mainConfiguration().sendMessage(sender, "menu-not-found", menuResolver));
    }

    @Subcommand("dump")
    @Description("Dumps the specified menu to the console.")
    @CommandPermission("vmenu.dump")
    @CommandCompletion("@menus")
    @Syntax("<menu>")
    public void onDump(CommandSender sender, String menu) {
        TagResolver menuResolver = Placeholder.parsed("menu", menu);

        this.plugin.configuration()
                .findByName(menu)
                .ifPresentOrElse(menuConfigurationFile -> {
                    this.plugin.mainConfiguration().sendMessage(sender, "dumping-menu", menuResolver);

                    try {
                        String content = Files.readString(menuConfigurationFile.path());
                        DumpUtils.createDump(content)
                                .whenComplete((url, throwable) -> {
                                    if (throwable != null) {
                                        this.plugin.mainConfiguration().sendMessage(sender, "dump-menu-failed", menuResolver);
                                        throwable.printStackTrace();
                                        return;
                                    }

                                    this.plugin.mainConfiguration().sendMessage(
                                            sender,
                                            "dump-menu-success",
                                            menuResolver,
                                            Placeholder.component("url", Component.text(url)
                                                    .hoverEvent(HoverEvent.showText(Component.text("Click to open the dump.")))
                                                    .clickEvent(ClickEvent.openUrl(url)))
                                    );
                                });
                    } catch (IOException e) {
                        this.plugin.mainConfiguration().sendMessage(sender, "dump-menu-failed", menuResolver);
                        throw new RuntimeException("Failed to read menu file", e);
                    }
                }, () -> this.plugin.mainConfiguration().sendMessage(sender, "menu-not-found", menuResolver));
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
