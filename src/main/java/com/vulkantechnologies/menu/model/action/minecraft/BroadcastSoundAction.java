package com.vulkantechnologies.menu.model.action.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.vulkantechnologies.menu.annotation.ComponentName;
import com.vulkantechnologies.menu.model.action.Action;
import com.vulkantechnologies.menu.model.menu.Menu;

import net.kyori.adventure.sound.Sound;

@ComponentName("broadcast-sound")
public record BroadcastSoundAction(Sound sound) implements Action {

    @Override
    public void accept(Player player, Menu menu) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(sound);
        }
    }
}
