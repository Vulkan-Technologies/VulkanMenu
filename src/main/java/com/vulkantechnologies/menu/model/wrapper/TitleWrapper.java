package com.vulkantechnologies.menu.model.wrapper;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;

public record TitleWrapper(ComponentWrapper title, @Nullable ComponentWrapper subtitle, int fadeIn, int stay,
                           int fadeOut) {

    public Title build(Player player, Menu menu) {
        Component title = this.title != null ? this.title.build(player, menu) : Component.empty();
        Component subtitle = this.subtitle != null ? this.subtitle.build(player, menu) : Component.empty();

        return Title.title(
                title,
                subtitle,
                Title.Times.times(
                        Ticks.duration(fadeIn),
                        Ticks.duration(stay),
                        Ticks.duration(fadeOut)
                )
        );
    }

}
